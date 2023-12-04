package com.example.projecthelper.service;

import com.aliyun.tea.TeaException;
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

import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;
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

    public List<Integer> getCnt(Long project_id, Long userId, Long identity){
        int projCnt;
        int ntcCnt;
        int assCnt;
        if(project_id == -1)
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
        else {
            projCnt = 0;
            switch (identity.intValue()){
                case 0:
                    ntcCnt = noticeMapper.getNtcCntByAdmAndProj(project_id);
                    assCnt = assignmentMapper.getAssCntByAdmAndProj(project_id);
                    break;
                case 1:
                    ntcCnt = noticeMapper.getNtcCntByTeaAndProj(userId, project_id);
                    assCnt = assignmentMapper.getAssCntByTeaAndProj(userId, project_id);
                    break;
                case 2:
                    ntcCnt = noticeMapper.getNtcCntByTaAndProj(userId, project_id);
                    assCnt = assignmentMapper.getAssCntByTaAndProj(userId, project_id);
                    break;
                case 3:
                    ntcCnt = noticeMapper.getNtcCntByStuAndProj(userId, project_id);
                    assCnt = assignmentMapper.getAssCntByStuAndProj(userId, project_id);
                    break;
                default:
                    return null;
            }
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

    public String verify_modify_code(KeyValueWrapper<Integer, KeyValueWrapper<String, String>> type_num_code, Long userId){
        String jwt = null;
        switch (type_num_code.getKey()){
            case 1 -> {
                break;
            }
            case 2 -> {
                jwt =  checkCode(type_num_code.getValue().getValue(), type_num_code.getValue().getKey(), FUNCTION.__MODIFY_NUMBER_, false);
                if(Objects.equals(
                    stringRedisTemplate.opsForValue().get(
                        getRedisKey(String.valueOf(userId), FUNCTION.__MODIFY_NUMBER_)
                    ), type_num_code.getValue().getKey()
                    )
                ){
                    stringRedisTemplate.delete(getRedisKey(String.valueOf(userId), FUNCTION.__MODIFY_NUMBER_));
                    usersMapper.updateEmail(userId, type_num_code.getValue().getKey());
                }
                else
                    throw new AccountFrozenException("无法修改改邮箱");
            }

        }
        return jwt;
    }

    public void sendCodeToChangeNumber(KeyValueWrapper<Integer, String> typeNum, Long userId){
        switch (typeNum.getKey()){
            case 1 -> {
                break;
            }
            case 2 -> {
                User user = usersMapper.findUserByMail(typeNum.getValue());
                if(user != null)
                    throw new InvalidFormException("邮件已经存在");
                sendMail(typeNum.getValue(), FUNCTION.__MODIFY_NUMBER_, false);
                stringRedisTemplate.opsForValue().set(
                    getRedisKey(String.valueOf(userId), FUNCTION.__MODIFY_NUMBER_),
                    typeNum.getValue(), Duration.ofMinutes(10)
                );
            }
        }
    }
    public String change_forget_password(KeyValueWrapper<KeyValueWrapper<Integer, String>, KeyValueWrapper<String, String>> type_pass_num_code){
        String jwt = null;
        switch (type_pass_num_code.getKey().getKey()){
            case 1 -> {
                break;
            }
            case 2 -> {
                jwt =  checkCode(type_pass_num_code.getValue().getValue(), type_pass_num_code.getValue().getKey(), FUNCTION.__FORGET_PASS__, true);
                usersMapper.changePassByEmail(
                    type_pass_num_code.getValue().getKey(),
                    passwordEncoder.encode(type_pass_num_code.getKey().getValue())
                );
            }

        }
        return jwt;
    }

    public void getForgetPassCode(KeyValueWrapper<Integer, String> typeNum){
        switch (typeNum.getKey()){
            case 1 -> {
                break;
            }
            case 2 -> {
                sendMail(typeNum.getValue(), FUNCTION.__FORGET_PASS__, true);
            }
        }
    }

    public String login_with_email(String code,String address){
        return checkCode(code, address, FUNCTION.__LOGIN__, true);
    }
    public void request_code(String to){
        sendMail(to, FUNCTION.__LOGIN__, true);
    }

    //检查验证码,返回ture为验证码正确，false为错误
    public String checkCode(String code,String address, FUNCTION function, boolean needJwt){

        String value = stringRedisTemplate.opsForValue().get(getRedisKey(address, function));

        if (value == null || !value.equals(code)){
            throw new InvalidFormException("验证码错误");
        }else {
            stringRedisTemplate.delete(getRedisKey(address, function));
            User user;
            if(needJwt){
                user = usersMapper.findUserByMail(address);
                return JWTUtil.createJWT(String.valueOf(user.getUserId()), String.valueOf(user.getIdentity()));
            }
            return null;
        }
    }
    public String checkCodeMassage(String code,String phone){
        User user = usersMapper.findUserByPhone(phone);
        String value = stringRedisTemplate.opsForValue().get(phone);

        if (value == null || !value.equals(code)){
            throw new InvalidFormException("验证码错误");
        }else {
            stringRedisTemplate.delete(phone);
            return JWTUtil.createJWT(String.valueOf(user.getUserId()), String.valueOf(user.getIdentity()));
        }
    }

    //发送带有验证码的邮件
    public void sendMail(String to, FUNCTION function, boolean mustExist) {
        if(mustExist){
            User user = usersMapper.findUserByMail(to);
            if(user == null)
                throw new InvalidFormException("邮箱错误");
            if(user.isFrozen())
                throw new AccountFrozenException("账户已冻结");
        }

        Random random = new Random();

        Long remainingTime = stringRedisTemplate.getExpire(getRedisKey(to, function));
        System.err.println(remainingTime);
        //发送后两分钟内不允许再次发送
        if (remainingTime > 8*60){
            throw new InvalidFormException("发送过于频繁");
        }
        StringBuilder sb = new StringBuilder();
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
        stringRedisTemplate.opsForValue().set(getRedisKey(to, function), code, Duration.ofMinutes(10));
    }

    private String getRedisKey(String number, FUNCTION function){
        return function.name()+number;
    }

    public enum FUNCTION{
        __FORGET_PASS__,
        __LOGIN__,
        __MODIFY_NUMBER_
    }

    public static com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId("LTAI5t9Ai1hwm7VcXdFYh8AE")
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret("KTnYB2D6IcfL7jthDlw8wp0o5EGTSl");
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }


    public void sendMassage(String to) throws Exception {
//        User user = usersMapper.findUserByPhone(to);
//        if(user == null)
//            throw new InvalidFormException("手机号错误");
//        if(user.isFrozen())
//            throw new AccountFrozenException("账户已冻结");

        Random random = new Random();

        long remainingTime = stringRedisTemplate.getExpire(to);

        //发送后两分钟内不允许再次发送
        if (remainingTime > 8*60){
            throw new InvalidFormException("发送过于频繁");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        String code = sb.toString();


//        java.util.List<String> args = java.util.Arrays.asList(args_);
        // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        com.aliyun.dysmsapi20170525.Client client = createClient();
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("张未硕的博客")
                .setTemplateCode("SMS_464081183")
                .setPhoneNumbers(to)
                .setTemplateParam("{\"code\":\""+code+"\"}");
        System.out.println("{\"code\":\""+code+"\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.sendSmsWithOptions(sendSmsRequest, runtime);
        } catch (TeaException error) {
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        }

        //发送邮件后开始计时,有效时间十分钟
        stringRedisTemplate.opsForValue().set(to, code, Duration.ofMinutes(10));
    }

            // 数据库功能测试


}
