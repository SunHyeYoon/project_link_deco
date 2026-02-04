package com.linkdeco.link_deco.service;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Template s3Template;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${spring.cloud.aws.s3.cloudfront-domain}")
    private String cloudFrontDomain;

    public S3Service(S3Template s3Template) {
        this.s3Template = s3Template;
    }

    public String upload(MultipartFile file) {
        String key = "static/images/profiles/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            upload(key, file.getInputStream());
            return cloudFrontDomain + "/" + key;
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패", e);
        }
    }

    public String upload(String key, InputStream inputStream) {
        S3Resource resource = s3Template.upload(bucketName, key, inputStream);
        try {
            return resource.getURL().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String key) {
        s3Template.deleteObject(bucketName, key);
    }
}
