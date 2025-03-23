package com.quizmaker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PDFContentDTO {
    private String text;
    private int pageCount;
    private String title;
    private String author;
    private String subject;
} 