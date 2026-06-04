package com.cloudsync.cloudsync.controller;

import com.cloudsync.cloudsync.entity.FileEntity;
import com.cloudsync.cloudsync.repository.FileRepository;
import com.cloudsync.cloudsync.service.FileService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    private final FileRepository fileRepository;

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file")
            MultipartFile file
    ) throws Exception {

        return fileService.uploadFile(file);
    }

    @GetMapping
    public List<FileEntity> getAllFiles() {

        return fileRepository.findAll();
    }
}