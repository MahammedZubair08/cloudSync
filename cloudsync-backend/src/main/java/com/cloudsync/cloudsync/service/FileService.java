package com.cloudsync.cloudsync.service;

import com.cloudsync.cloudsync.entity.FileEntity;
import com.cloudsync.cloudsync.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Client s3Client;
    private final FileRepository fileRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file)
            throws IOException {

        String fileName =
                UUID.randomUUID() + "_" +
                        file.getOriginalFilename();

        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(file.getContentType())
                        .build();

        s3Client.putObject(
                putObjectRequest,
                software.amazon.awssdk.core.sync.RequestBody
                        .fromBytes(file.getBytes())
        );

        String fileUrl =
                "https://" + bucketName +
                        ".s3.amazonaws.com/" +
                        fileName;

        FileEntity fileEntity = FileEntity.builder()
                .fileName(file.getOriginalFilename())
                .fileUrl(fileUrl)
                .s3Key(fileName)
                .size(file.getSize())
                .contentType(file.getContentType())
                .uploadedAt(LocalDateTime.now())
                .build();

        fileRepository.save(fileEntity);

        return fileUrl;
    }
}