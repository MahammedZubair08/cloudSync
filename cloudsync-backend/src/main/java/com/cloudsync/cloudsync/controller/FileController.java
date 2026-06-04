package com.cloudsync.cloudsync.controller;

import com.cloudsync.cloudsync.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file")
            MultipartFile file
    ) throws Exception {

        return fileService.uploadFile(file);
    }
}