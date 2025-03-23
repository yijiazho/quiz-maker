package com.quizmaker.service;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.quizmaker.dto.PDFContentDTO;

@ExtendWith(MockitoExtension.class)
class PDFIngestionServiceTest {

    @InjectMocks
    private PDFIngestionService pdfIngestionService;

    private MockMultipartFile validPdfFile;
    private MockMultipartFile emptyFile;
    private MockMultipartFile nonPdfFile;
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        // Create a simple PDF-like content
        String pdfContent = "%PDF-1.4\n" +
                           "1 0 obj\n" +
                           "<</Type/Catalog/Pages 2 0 R>>\n" +
                           "endobj\n" +
                           "2 0 obj\n" +
                           "<</Type/Pages/Kids[3 0 R]/Count 1>>\n" +
                           "endobj\n" +
                           "3 0 obj\n" +
                           "<</Type/Page/MediaBox[0 0 612 792]/Parent 2 0 R/Resources<<>>>>\n" +
                           "endobj\n" +
                           "xref\n" +
                           "0 4\n" +
                           "0000000000 65535 f\n" +
                           "trailer\n" +
                           "<</Size 4/Root 1 0 R>>\n" +
                           "startxref\n" +
                           "0\n" +
                           "%%EOF";

        validPdfFile = new MockMultipartFile(
            "file",
            "test.pdf",
            "application/pdf",
            pdfContent.getBytes()
        );

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
            "This is not a PDF file".getBytes()
        );

        mockFile = new MockMultipartFile(
            "file",
            "test.pdf",
            "application/pdf",
            "%PDF-1.4\ntest content".getBytes()
        );
    }

    @Test
    void extractContent_ValidFile_ReturnsContent() throws IOException {
        PDFContentDTO result = pdfIngestionService.extractContent(validPdfFile);
        
        assertNotNull(result);
        assertNotNull(result.getText());
        assertEquals("Untitled", result.getTitle());
        assertEquals("Unknown", result.getAuthor());
        assertEquals("", result.getSubject());
        assertTrue(result.getPageCount() > 0);
    }

    @Test
    void extractContent_EmptyFile_ThrowsException() {
        assertThrows(IOException.class, () -> pdfIngestionService.extractContent(emptyFile));
    }

    @Test
    void extractContent_NonPDFFile_ThrowsException() {
        assertThrows(IOException.class, () -> pdfIngestionService.extractContent(nonPdfFile));
    }

    @Test
    void extractPageContent_ValidPage_ReturnsContent() throws IOException {
        PDFContentDTO result = pdfIngestionService.extractPageContent(validPdfFile, 1);
        assertNotNull(result);
    }

    @Test
    void extractPageContent_InvalidPage_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            pdfIngestionService.extractPageContent(validPdfFile, 0));
        assertThrows(IllegalArgumentException.class, () -> 
            pdfIngestionService.extractPageContent(validPdfFile, 2));
    }
} 