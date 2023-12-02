package com.example.projecthelper.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class OssService {

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucketName}")
    private String bucketName;

    public String defaultAvatarUrl(){
        return "https://" + bucketName + "." + endpoint + "/" + "avatar/" + "default" + "/" + "avatar1.jpg";
    }

    public String toUrl(String originalFileName, Long userId){
        return  "https://" + bucketName + "." + endpoint + "/" + "avatar/" + userId + "/" + originalFileName;
    }

    public String uploadImage(MultipartFile file, Long userId) {
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 文件存储入OSS
            String fileName = "avatar/" + userId + "/" + file.getOriginalFilename(); // 可以设置文件路径和名称
            ossClient.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream()));

            // 返回文件的URL地址
            return "https://" + bucketName + "." + endpoint + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // 关闭OSSClient
            ossClient.shutdown();
        }
    }

    public boolean deleteImage(String fileName) {
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 检查文件是否存在
            if (!ossClient.doesObjectExist(bucketName, fileName)) {
                return false;
            }

            // 从OSS中删除文件
            ossClient.deleteObject(bucketName, fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // 关闭OSSClient
            ossClient.shutdown();
        }
    }

}
