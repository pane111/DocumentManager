package com.fhtw.rest.controller;

import com.fhtw.rest.dto.DocumentDto;
import com.fhtw.rest.service.DocumentService;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/docs")
public class DocumentController {
    private final DocumentService service;
    Logger logger = LogManager.getLogger(DocumentController.class);
    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @GetMapping
    public List<DocumentDto> getAll() {
        logger.info("Request to get all documents");
        return service.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getById(@PathVariable Long id) {
        DocumentDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    /*
    @PostMapping
    public ResponseEntity<DocumentDto> create(@RequestBody DocumentDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }
    */
    @PostMapping
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) throws Exception {
        if (!file.getOriginalFilename().endsWith(".pdf")) {
            return ResponseEntity.badRequest().body("Only .pdf files are supported");
        }
        service.uploadFile(file);
        return ResponseEntity.ok("Sent file " + file.getOriginalFilename());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDto> update(@PathVariable Long id, @RequestBody DocumentDto dto) {
        DocumentDto updated = service.update(id, dto);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
