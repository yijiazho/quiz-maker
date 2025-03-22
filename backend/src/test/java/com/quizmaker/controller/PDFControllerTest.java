package com.quizmaker.controller;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.quizmaker.model.PDFContent;
import com.quizmaker.service.PDFIngestionService;

@SpringBootTest
@AutoConfigureMockMvc
class PDFControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PDFIngestionService pdfIngestionService;

    private MockMultipartFile testFile;
    private PDFContent testContent;

    @BeforeEach
    void setUp() {
        testFile = new MockMultipartFile(
            "file",
            "test.pdf",
            "application/pdf",
            "test content".getBytes()
        );

        testContent = PDFContent.builder()
            .text("Test content")
            .pageCount(1)
            .title("Test PDF")
            .author("Test Author")
            .subject("Test Subject")
            .build();
    }

    @Test
    void extractContent_WithValidFile_ShouldReturnOk() throws Exception {
        when(pdfIngestionService.extractContent(any())).thenReturn(testContent);

        mockMvc.perform(multipart("/api/pdf/extract")
                .file(testFile))
                .andExpect(status().isOk());
    }

    @Test
    void extractContent_WhenServiceThrowsIllegalArgument_ShouldReturnBadRequest() throws Exception {
        when(pdfIngestionService.extractContent(any()))
            .thenThrow(new IllegalArgumentException("Invalid file"));

        mockMvc.perform(multipart("/api/pdf/extract")
                .file(testFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void extractContent_WhenServiceThrowsIOException_ShouldReturnInternalError() throws Exception {
        when(pdfIngestionService.extractContent(any()))
            .thenThrow(new IOException("Processing error"));

        mockMvc.perform(multipart("/api/pdf/extract")
                .file(testFile))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void extractPageContent_WithValidFileAndPage_ShouldReturnOk() throws Exception {
        when(pdfIngestionService.extractTextFromPage(any(), eq(1)))
            .thenReturn("Page 1 content");

        mockMvc.perform(multipart("/api/pdf/extract/page/1")
                .file(testFile))
                .andExpect(status().isOk());
    }

    @Test
    void extractPageContent_WithInvalidPage_ShouldReturnBadRequest() throws Exception {
        when(pdfIngestionService.extractTextFromPage(any(), eq(0)))
            .thenThrow(new IllegalArgumentException("Invalid page number"));

        mockMvc.perform(multipart("/api/pdf/extract/page/0")
                .file(testFile))
                .andExpect(status().isBadRequest());
    }
} 