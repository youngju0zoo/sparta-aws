package com.example.spartaaws.s3;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Service
public class S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public S3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }
    public String uploadImage(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket("sparta-aws-profile-0zoo")
                .key(fileName).contentType(file.getContentType()).build();
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패", e);
        }
        String imageUrl = s3Client.utilities().getUrl(builder -> builder
                .bucket("sparta-aws-profile-0zoo").key(fileName)).toExternalForm();
        return imageUrl;
    }
    public String getPresignedUrl(String imageUrl) {
        String fileName = s3Client.utilities().parseUri(URI.create(imageUrl)).key()
                .orElseThrow(() -> new RuntimeException("S3 이미지 key를 찾을 수 없습니다."));
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("sparta-aws-profile-0zoo").key(fileName).build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(7)).getObjectRequest(getObjectRequest).build();
        String presignedUrl = s3Presigner.presignGetObject(presignRequest).url().toExternalForm();
        return presignedUrl;
    }
}
