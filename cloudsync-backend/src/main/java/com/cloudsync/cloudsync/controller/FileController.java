package com.cloudsync.cloudsync.controller;

import com.cloudsync.cloudsync.entity.FileEntity;
import com.cloudsync.cloudsync.repository.FileRepository;
import com.cloudsync.cloudsync.service.FileService;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
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
public FileEntity uploadFile(
        @RequestParam("file") MultipartFile file,
        Authentication authentication
) throws IOException {

    return fileService.uploadFile(
            file,
            authentication.getName()
    );
}
    @GetMapping
    public List<FileEntity> getAllFiles(Authentication authentication) {

        return fileRepository.findByUserEmail(authentication.getName());
    }

    @GetMapping("/download/{id}")
    public String downloadFile(
            @PathVariable Long id,
            Authentication authentication) {

        return fileService.generatePresignedUrl(
                id,
                authentication.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteFile(
            @PathVariable Long id,
            Authentication authentication) {

        fileService.deleteFile(
                id,
                authentication.getName());
    }
}