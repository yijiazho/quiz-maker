package com.quizmaker.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.quizmaker.dto.PDFContentDTO;
import com.quizmaker.service.PDFIngestionService;

@SpringBootTest
@AutoConfigureMockMvc
class PDFControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PDFIngestionService pdfIngestionService;

    private MockMultipartFile pdfFile;
    private PDFContentDTO mockContent;
    private static final String TEST_STORAGE_DIR = "pdf_contents";

    @BeforeEach
    void setUp() {
        pdfFile = new MockMultipartFile(
            "file",
            "test.pdf",
            "application/pdf",
            "test content".getBytes()
        );

        mockContent = PDFContentDTO.builder()
                .text("Test PDF Content")
                .pageCount(1)
                .title("Test PDF")
                .author("Test Author")
                .subject("Test Subject")
                .build();
    }

    @AfterEach
    void cleanup() {
        try {
            Path storagePath = Paths.get(TEST_STORAGE_DIR);
            if (Files.exists(storagePath)) {
                try (Stream<Path> paths = Files.walk(storagePath)) {
                    paths.filter(path -> path.toString().endsWith(".json"))
                         .forEach(path -> {
                             try {
                                 Files.delete(path);
                                 System.out.println("Deleted test JSON file: " + path);
                             } catch (IOException e) {
                                 System.err.println("Failed to delete JSON file: " + path + " - " + e.getMessage());
                             }
                         });
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to clean up test JSON files: " + e.getMessage());
        }
    }

    @Test
    @WithMockUser
    void extractContent_ValidFile_ReturnsContent() throws Exception {
        when(pdfIngestionService.extractContent(any())).thenReturn(mockContent);

        mockMvc.perform(multipart("/api/pdf/extract")
                .file(pdfFile)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test PDF Content"))
                .andExpect(jsonPath("$.pageCount").value(1))
                .andExpect(jsonPath("$.title").value("Test PDF"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.subject").value("Test Subject"));
    }

    @Test
    @WithMockUser
    void extractPageContent_ValidPage_ReturnsContent() throws Exception {
        when(pdfIngestionService.extractPageContent(any(), anyInt())).thenReturn(mockContent);

        mockMvc.perform(multipart("/api/pdf/extract/page/1")
                .file(pdfFile)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test PDF Content"))
                .andExpect(jsonPath("$.pageCount").value(1))
                .andExpect(jsonPath("$.title").value("Test PDF"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.subject").value("Test Subject"));
    }

    @Test
    @WithMockUser
    void extractPageContent_InvalidPage_ReturnsBadRequest() throws Exception {
        when(pdfIngestionService.extractPageContent(any(), anyInt()))
            .thenThrow(new IllegalArgumentException("Invalid page number"));

        mockMvc.perform(multipart("/api/pdf/extract/page/0")
                .file(pdfFile)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.text").value("Invalid page number: Invalid page number"))
                .andExpect(jsonPath("$.pageCount").value(0))
                .andExpect(jsonPath("$.title").value("Error"))
                .andExpect(jsonPath("$.author").value("System"))
                .andExpect(jsonPath("$.subject").value("Error"));
    }

    @Test
    @WithMockUser
    void extractContent_WhenServiceThrowsIOException_ShouldReturnInternalError() throws Exception {
        when(pdfIngestionService.extractContent(any()))
            .thenThrow(new IOException("Processing error"));

        mockMvc.perform(multipart("/api/pdf/extract")
                .file(pdfFile)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.text").value("Error processing PDF file: Processing error"))
                .andExpect(jsonPath("$.pageCount").value(0))
                .andExpect(jsonPath("$.title").value("Error"))
                .andExpect(jsonPath("$.author").value("System"))
                .andExpect(jsonPath("$.subject").value("Error"));
    }
} 