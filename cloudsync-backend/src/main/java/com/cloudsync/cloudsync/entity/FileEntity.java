package com.cloudsync.cloudsync.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.cloudsync.cloudsync.entity.User;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileUrl;

    private String s3Key;

    private Long size;

    private String contentType;

    private LocalDateTime uploadedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

}