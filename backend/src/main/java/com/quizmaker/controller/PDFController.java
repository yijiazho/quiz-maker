package com.quizmaker.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quizmaker.dto.PDFContentDTO;
import com.quizmaker.service.FileStorageService;
import com.quizmaker.service.PDFIngestionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/pdf")
public class PDFController {

    private final PDFIngestionService pdfIngestionService;
    private final FileStorageService fileStorageService;

    public PDFController(PDFIngestionService pdfIngestionService, FileStorageService fileStorageService) {
        this.pdfIngestionService = pdfIngestionService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/extract")
    public ResponseEntity<PDFContentDTO> extractContent(@RequestParam("file") MultipartFile file) {
        log.info("Received PDF extraction request - File name: {}, Size: {} bytes, Content type: {}", 
                file.getOriginalFilename(), file.getSize(), file.getContentType());
        
        try {
            log.debug("Starting PDF content extraction");
            PDFContentDTO content = pdfIngestionService.extractContent(file);
            log.info("Successfully extracted PDF content - Title: {}, Author: {}, Page count: {}", 
                    content.getTitle(), content.getAuthor(), content.getPageCount());
            
            // Save the extracted content to a JSON file
            String savedFileName = fileStorageService.savePDFContent(content, file.getOriginalFilename());
            log.info("Saved PDF content to file: {}", savedFileName);
            
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            log.error("Failed to process PDF file: {}", e.getMessage(), e);
            PDFContentDTO errorContent = PDFContentDTO.builder()
                .text("Error processing PDF file: " + e.getMessage())
                .pageCount(0)
                .title("Error")
                .author("System")
                .subject("Error")
                .build();
            return ResponseEntity.internalServerError().body(errorContent);
        }
    }

    @PostMapping("/extract/page/{pageNumber}")
    public ResponseEntity<PDFContentDTO> extractPageContent(
            @RequestParam("file") MultipartFile file,
            @PathVariable int pageNumber) {
        log.info("Received page extraction request - File name: {}, Page number: {}, Size: {} bytes", 
                file.getOriginalFilename(), pageNumber, file.getSize());
        
        try {
            log.debug("Starting page content extraction");
            PDFContentDTO content = pdfIngestionService.extractPageContent(file, pageNumber);
            log.info("Successfully extracted page content - Title: {}, Author: {}, Page count: {}", 
                    content.getTitle(), content.getAuthor(), content.getPageCount());
            
            // Save the extracted content to a JSON file
            String savedFileName = fileStorageService.savePDFContent(content, 
                String.format("%s_page%d", file.getOriginalFilename(), pageNumber));
            log.info("Saved page content to file: {}", savedFileName);
            
            return ResponseEntity.ok(content);
        } catch (IllegalArgumentException e) {
            log.error("Invalid page number requested: {}", e.getMessage());
            PDFContentDTO errorContent = PDFContentDTO.builder()
                .text("Invalid page number: " + e.getMessage())
                .pageCount(0)
                .title("Error")
                .author("System")
                .subject("Error")
                .build();
            return ResponseEntity.badRequest().body(errorContent);
        } catch (IOException e) {
            log.error("Failed to process PDF file for page extraction: {}", e.getMessage(), e);
            PDFContentDTO errorContent = PDFContentDTO.builder()
                .text("Error processing PDF file: " + e.getMessage())
                .pageCount(0)
                .title("Error")
                .author("System")
                .subject("Error")
                .build();
            return ResponseEntity.internalServerError().body(errorContent);
        }
    }
} 