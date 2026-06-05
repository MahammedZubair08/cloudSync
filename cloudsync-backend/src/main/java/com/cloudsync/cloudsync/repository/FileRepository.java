package com.cloudsync.cloudsync.repository;

import com.cloudsync.cloudsync.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository
        extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByUserEmail(String email);
}