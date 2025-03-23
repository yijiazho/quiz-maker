package com.quizmaker.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizmaker.dto.PDFContentDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileStorageService {
    private static final String STORAGE_DIR = "pdf_contents";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public FileStorageService() {
        createStorageDirectory();
    }

    private void createStorageDirectory() {
        try {
            Path storagePath = Paths.get(STORAGE_DIR);
            if (!Files.exists(storagePath)) {
                Files.createDirectory(storagePath);
                log.info("Created storage directory: {}", storagePath.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Failed to create storage directory: {}", e.getMessage(), e);
        }
    }

    public String savePDFContent(PDFContentDTO content, String originalFileName) {
        try {
            String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
            String fileName = String.format("%s_%s.json", 
                originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_"), 
                timestamp);
            
            Path filePath = Paths.get(STORAGE_DIR, fileName);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(filePath.toFile(), content);
            
            log.info("Saved PDF content to: {}", filePath.toAbsolutePath());
            return fileName;
        } catch (IOException e) {
            log.error("Failed to save PDF content: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save PDF content", e);
        }
    }
} 