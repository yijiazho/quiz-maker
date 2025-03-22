package com.quizmaker.service;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.quizmaker.model.PDFContent;

@ExtendWith(MockitoExtension.class)
class PDFIngestionServiceTest {

    @InjectMocks
    private PDFIngestionService pdfIngestionService;

    private MockMultipartFile validPdfFile;
    private MockMultipartFile emptyFile;
    private MockMultipartFile nonPdfFile;

    @BeforeEach
    void setUp() {
        // Load a test PDF file from resources
        try (InputStream is = getClass().getResourceAsStream("/test.pdf")) {
            validPdfFile = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                is != null ? is.readAllBytes() : new byte[0]
            );
        } catch (IOException e) {
            fail("Could not load test PDF file");
        }

        emptyFile = new MockMultipartFile(
            "file",
            "empty.pdf",
            "application/pdf",
            new byte[0]
        );

        nonPdfFile = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "test content".getBytes()
        );
    }

    @Test
    void extractContent_WithValidPDF_ShouldReturnContent() {
        assertDoesNotThrow(() -> {
            PDFContent content = pdfIngestionService.extractContent(validPdfFile);
            assertNotNull(content);
            assertNotNull(content.getText());
            assertTrue(content.getPageCount() > 0);
        });
    }

    @Test
    void extractContent_WithEmptyFile_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            pdfIngestionService.extractContent(emptyFile)
        );
        assertEquals("Provided file is empty", exception.getMessage());
    }

    @Test
    void extractContent_WithNonPDFFile_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            pdfIngestionService.extractContent(nonPdfFile)
        );
        assertEquals("File must be a PDF", exception.getMessage());
    }

    @Test
    void extractTextFromPage_WithInvalidPageNumber_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            pdfIngestionService.extractTextFromPage(validPdfFile, 0)
        );
        assertEquals("Page number must be greater than 0", exception.getMessage());
    }

    @Test
    void extractTextFromPage_WithValidPageNumber_ShouldReturnText() {
        assertDoesNotThrow(() -> {
            String text = pdfIngestionService.extractTextFromPage(validPdfFile, 1);
            assertNotNull(text);
            assertFalse(text.isEmpty());
        });
    }
} 