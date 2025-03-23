package com.quizmaker.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quizmaker.dto.PDFContentDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PDFIngestionService {

    public PDFContentDTO extractContent(MultipartFile file) throws IOException {
        log.debug("Starting PDF content extraction process");
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            
            log.debug("PDF document loaded successfully, processing content");
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            log.debug("Text content extracted, length: {} characters", text.length());
            
            PDDocumentInformation info = document.getDocumentInformation();
            log.debug("Document metadata retrieved - Title: {}, Author: {}, Subject: {}", 
                    info.getTitle(), info.getAuthor(), info.getSubject());
            
            PDFContentDTO result = PDFContentDTO.builder()
                    .text(text)
                    .pageCount(document.getNumberOfPages())
                    .title(info.getTitle() != null ? info.getTitle() : "Untitled")
                    .author(info.getAuthor() != null ? info.getAuthor() : "Unknown")
                    .subject(info.getSubject() != null ? info.getSubject() : "")
                    .build();
            
            log.debug("PDF content extraction completed successfully");
            return result;
        } catch (IOException e) {
            log.error("Error during PDF content extraction: {}", e.getMessage(), e);
            throw e;
        }
    }

    public PDFContentDTO extractPageContent(MultipartFile file, int pageNumber) throws IOException {
        log.debug("Starting page content extraction process for page {}", pageNumber);
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            
            int totalPages = document.getNumberOfPages();
            log.debug("PDF document loaded successfully, total pages: {}", totalPages);
            
            if (pageNumber < 1 || pageNumber > totalPages) {
                String errorMsg = String.format("Invalid page number: %d (valid range: 1-%d)", pageNumber, totalPages);
                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(pageNumber);
            stripper.setEndPage(pageNumber);
            
            String text = stripper.getText(document);
            log.debug("Page {} content extracted, length: {} characters", pageNumber, text.length());
            
            PDDocumentInformation info = document.getDocumentInformation();
            log.debug("Document metadata retrieved - Title: {}, Author: {}, Subject: {}", 
                    info.getTitle(), info.getAuthor(), info.getSubject());
            
            PDFContentDTO result = PDFContentDTO.builder()
                    .text(text)
                    .pageCount(totalPages)
                    .title(info.getTitle() != null ? info.getTitle() : "Untitled")
                    .author(info.getAuthor() != null ? info.getAuthor() : "Unknown")
                    .subject(info.getSubject() != null ? info.getSubject() : "")
                    .build();
            
            log.debug("Page content extraction completed successfully");
            return result;
        } catch (IOException e) {
            log.error("Error during page content extraction: {}", e.getMessage(), e);
            throw e;
        }
    }
} 