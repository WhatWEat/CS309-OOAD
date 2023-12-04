package com.example.projecthelper.service;
import com.aliyun.tea.TeaException;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
//import com.aliyuncs.sms.model.v20170525.SendSmsRequest;
//import com.aliyuncs.sms.model.v20170525.SendSmsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class smsService {

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.signName}")
    private String signName;

    @Value("${aliyun.sms.templateCode}")
    private String templateCode;

//    public SendSmsResponse sendSms(String phoneNumber, String templateParam) throws ClientException {
//        // 创建DefaultAcsClient实例并初始化
//        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
//        IAcsClient acsClient = new DefaultAcsClient(profile);
//
//        // 组装请求对象
//        SendSmsRequest request = new SendSmsRequest();
//        request.setPhoneNumbers(phoneNumber);
//        request.setSignName(signName);
//        request.setTemplateCode(templateCode);
//        request.setTemplateParam(templateParam);
//
//        // 发送短信并处理响应
//        return acsClient.getAcsResponse(request);
//    }
}
