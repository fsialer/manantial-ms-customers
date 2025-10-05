package com.fernando.manantial_ms_customers.infrastructure.adapters.output.storage.facade;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class AwsS3StorageDrive implements StorageDrive{

    @Value("${amazon.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Override
    public byte[] getFile(String path) {
        try {
            var s3Object = amazonS3.getObject(bucket, path);
            var inputStream = s3Object.getObjectContent();
            return inputStream.readAllBytes();
        } catch (Exception e) {
            log.error("❌ Error downloading file from AWS S3: {}", path, e);
            throw new RuntimeException("Error downloading file from AWS S3", e);
        }
    }
}
