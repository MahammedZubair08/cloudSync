package com.cloudsync.cloudsync.service;

import com.cloudsync.cloudsync.entity.FileEntity;
import com.cloudsync.cloudsync.entity.User;
import com.cloudsync.cloudsync.repository.FileRepository;
import com.cloudsync.cloudsync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

        private final S3Client s3Client;
        private final FileRepository fileRepository;
        private final S3Presigner s3Presigner;
        private final UserRepository userRepository;
        @Value("${aws.s3.bucket}")
        private String bucketName;

        public FileEntity uploadFile(MultipartFile file,String email)
                        throws IOException {

                String fileName = UUID.randomUUID() + "_" +
                                file.getOriginalFilename();

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(fileName)
                                .contentType(file.getContentType())
                                .build();

                s3Client.putObject(
                                putObjectRequest,
                                software.amazon.awssdk.core.sync.RequestBody
                                                .fromBytes(file.getBytes()));

                String fileUrl = "https://" + bucketName +
                                ".s3.amazonaws.com/" +
                                fileName;

                User user = userRepository.findByEmail(email)
                                .orElseThrow();

                FileEntity fileEntity = FileEntity.builder()
                                .fileName(file.getOriginalFilename())
                                .fileUrl(fileUrl)
                                .s3Key(fileName)
                                .size(file.getSize())
                                .contentType(file.getContentType())
                                .user(user)
                                .uploadedAt(LocalDateTime.now())
                                .build();

                fileRepository.save(fileEntity);

                return fileEntity;
        }

        public String generatePresignedUrl(
                        Long fileId,
                        String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow();

                FileEntity file = fileRepository.findById(fileId)
                                .orElseThrow();

                if (!file.getUser().getId()
                                .equals(user.getId())) {

                        throw new RuntimeException(
                                        "Unauthorized access");
                }

                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                .bucket(bucketName)
                                .key(file.getS3Key())
                                .build();

                GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                                .signatureDuration(
                                                java.time.Duration.ofMinutes(10))
                                .getObjectRequest(getObjectRequest)
                                .build();

                return s3Presigner
                                .presignGetObject(presignRequest)
                                .url()
                                .toString();
        }

        public void deleteFile(Long fileId, String email) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow();

                FileEntity file = fileRepository.findById(fileId)
                                .orElseThrow();

                if (!file.getUser().getId()
                                .equals(user.getId())) {
                        throw new RuntimeException(
                                        "Unauthorized access");
                }

                DeleteObjectRequest deleteObjectRequest =
                                DeleteObjectRequest.builder()
                                                .bucket(bucketName)
                                                .key(file.getS3Key())
                                                .build();

                s3Client.deleteObject(deleteObjectRequest);

                fileRepository.delete(file);
        }
}