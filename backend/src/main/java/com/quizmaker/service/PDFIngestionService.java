package com.quizmaker.service;

import com.quizmaker.model.PDFContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class PDFIngestionService {

    public PDFContent extractContent(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Provided file is empty");
        }

        if (!file.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("File must be a PDF");
        }

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            PDDocumentInformation info = document.getDocumentInformation();

            return PDFContent.builder()
                    .text(text)
                    .pageCount(document.getNumberOfPages())
                    .title(info.getTitle())
                    .author(info.getAuthor())
                    .subject(info.getSubject())
                    .build();
        } catch (IOException e) {
            log.error("Error processing PDF file: {}", e.getMessage());
            throw new IOException("Failed to process PDF file", e);
        }
    }

    public String extractTextFromPage(MultipartFile file, int pageNumber) throws IOException {
        if (pageNumber < 1) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            if (pageNumber > document.getNumberOfPages()) {
                throw new IllegalArgumentException("Page number exceeds document length");
            }

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(pageNumber);
            stripper.setEndPage(pageNumber);

            return stripper.getText(document);
        }
    }
} 