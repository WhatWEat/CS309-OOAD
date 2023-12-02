package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.AccountFrozenException;
import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.config.RedisConfig;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.AssignmentMapper;
import com.example.projecthelper.mapper.NoticeMapper;
import com.example.projecthelper.mapper.ProjectMapper;
import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.util.FileUtil;
import com.example.projecthelper.util.JWTUtil;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private FileService fileService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OssService ossService;
    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //TODO:更新个人信息
    public void editPersonInfo(User user, String jwt){
        //此处mapper中传参已经改为user，可以更改名字、身份、id、性别外的所有信息
        try {
            user.setUserId(Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
            user.setProgrammingSkills(
                user.getProgrammingSkills() == null ? new ArrayList<>() : user.getProgrammingSkills()
            );
            if(user.getAvatar() != null){
                fileService.removeOriAvatar(Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
//                String path = FileUtil.generateAvatarPath(user.getUserId());
//                String avP = FileUtil.saveFile(path, user.getAvatar().getOriginalFilename(), user.getAvatar());
                String avP = ossService.uploadImage(user.getAvatar(), user.getUserId());
                user.setAvatarPath(user.getAvatar().getOriginalFilename());
            }
            else {
                user.setAvatarPath(usersMapper.findUserById(Long.parseLong(JWTUtil.getUserIdByToken(jwt))).getAvatarPath());
            }
            usersMapper.updateStuInformation(user);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getPersonInfo(Long userId){
        User user = usersMapper.findUserById(userId);
        System.err.println(user);
        if(user != null){
            if(user.getAvatarPath() != null)
                user.setAvatarPath(ossService.toUrl(user.getAvatarPath(), userId));
            else
                user.setAvatarPath(ossService.defaultAvatarUrl());
            user.setPassword(null);
        }
        else
            throw new InvalidFormException("找不到用户");
        return user;
    }

    public List<User> getUsersByIdentity(int identity, Long projId){
        if(projId == -1)
            return usersMapper.findUsersByIdentity(identity);
        else {
            if(identity == 2)
                return usersMapper.findTaByProj(projId);
        }
        return null;
    }

    //test
    public String getPersonName(Long userId){
        User user = usersMapper.findUserById(userId);
        System.err.println(user);
        if (user != null){
            return user.getName();
        }else {
            throw new InvalidFormException("找不到用户");
        }
    }

    public List<Integer> getCnt(Long userId, Long identity){
        int projCnt;
        int ntcCnt;
        int assCnt;
        switch (identity.intValue()){
            case 0:
                projCnt = projectMapper.getProjCntByAdm();
                ntcCnt = noticeMapper.getNtcCntByAdm();
                assCnt = assignmentMapper.getAssCntByAdm();
                break;
            case 1:
                projCnt = projectMapper.getProjCntByTea(userId);
                ntcCnt = noticeMapper.getNtcCntByTea(userId);
                assCnt = assignmentMapper.getAssCntByTea(userId);
                break;
            case 2:
                projCnt = projectMapper.getProjCntByTa(userId);
                ntcCnt = noticeMapper.getNtcCntByTa(userId);
                assCnt = assignmentMapper.getAssCntByTa(userId);
                break;
            case 3:
                projCnt = projectMapper.getProjCntByStu(userId);
                ntcCnt = noticeMapper.getNtcCntByStu(userId);
                assCnt = assignmentMapper.getAssCntByStu(userId);
                break;
            default:
                return null;
        }
        List<Integer> cnt = new ArrayList<>();
        cnt.add(projCnt);
        cnt.add(ntcCnt);
        cnt.add(assCnt);
        return cnt;
    }

//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.qq.com");
//        mailSender.setPort(587);
//        mailSender.setUsername("1366758028@qq.com");
//        mailSender.setPassword("cpinybjvtinjfiij");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }

    //检查验证码,返回ture为验证码正确，false为错误
    public String checkCode(String code,String address){
        User user = usersMapper.findUserByMail(address);
        String value = stringRedisTemplate.opsForValue().get(address);

        if (value == null || !value.equals(code)){
            throw new InvalidFormException("验证码错误");
        }else {
            stringRedisTemplate.delete(address);
            return JWTUtil.createJWT(String.valueOf(user.getUserId()), String.valueOf(user.getIdentity()));
        }
    }



    //发送带有验证码的邮件
    public void sendMail(String to) {
        User user = usersMapper.findUserByMail(to);
        if(user == null)
            throw new InvalidFormException("邮箱错误");
        if(user.isFrozen())
            throw new AccountFrozenException("账户已冻结");

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        long remainingTime = stringRedisTemplate.getExpire(to);

        //发送后两分钟内不允许再次发送
        if (remainingTime > 8*60){
            throw new InvalidFormException("发送过于频繁");
        }

        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        String code = sb.toString();

        //编辑邮件内容
        try{
            String subject = "ProjectHelper Verification Code";
            String content = "The verification code for your current operation is " +code+".\n"+
                "It will expire in ten minutes or after one use.\n"+
                "\n"+
                "Thank you!\n"+
                "The ProjectHelper team";
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("1366758028@qq.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
        }catch (
            Exception e
        ){
            throw new InvalidFormException("地址无效");
        }


        //发送邮件后开始计时,有效时间十分钟
        stringRedisTemplate.opsForValue().set(to, code, Duration.ofMinutes(10));
    }




    
    // 数据库功能测试


}
