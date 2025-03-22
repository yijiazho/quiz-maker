package com.quizmaker.model;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class PDFContent {
    private String text;
    private int pageCount;
    private String title;
    private String author;
    private String subject;
} 