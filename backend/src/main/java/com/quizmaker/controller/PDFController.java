package com.quizmaker.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quizmaker.model.PDFContent;
import com.quizmaker.service.PDFIngestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
public class PDFController {

    private final PDFIngestionService pdfIngestionService;

    @PostMapping("/extract")
    public ResponseEntity<PDFContent> extractContent(@RequestParam("file") MultipartFile file) {
        try {
            PDFContent content = pdfIngestionService.extractContent(file);
            return ResponseEntity.ok(content);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/extract/page/{pageNumber}")
    public ResponseEntity<String> extractPageContent(
            @RequestParam("file") MultipartFile file,
            @PathVariable int pageNumber) {
        try {
            String content = pdfIngestionService.extractTextFromPage(file, pageNumber);
            return ResponseEntity.ok(content);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 