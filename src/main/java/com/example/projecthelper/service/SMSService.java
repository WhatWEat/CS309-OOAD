package com.example.projecthelper.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSService {

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.sms.templateCode}")
    private String templateCode;
    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception
     */
    public com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
            // 必填，您的 AccessKey ID
            .setAccessKeyId(accessKeyId)
            // 必填，您的 AccessKey Secret
            .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public void send_code() throws Exception {
        com.aliyun.dysmsapi20170525.Client client = createClient();
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
            .setPhoneNumbers("13965693572")
            .setSignName("张未硕的博客")
            .setTemplateCode(templateCode)
            .setTemplateParam("{\"code\":\"1234\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        com.aliyun.dysmsapi20170525.models.SendSmsResponse resp = client.sendSmsWithOptions(sendSmsRequest, runtime);
        System.err.println(resp.statusCode);
        System.err.println(accessKeyId+" "+accessKeySecret+" "+templateCode);
        System.err.println(resp.body.getMessage());
    }


}
