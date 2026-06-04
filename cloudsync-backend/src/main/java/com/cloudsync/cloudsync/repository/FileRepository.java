package com.cloudsync.cloudsync.repository;

import com.cloudsync.cloudsync.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository
        extends JpaRepository<FileEntity, Long> {
}