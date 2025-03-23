import React, { useState } from 'react';
import { Container, Typography, Box, Paper } from '@mui/material';
import { PDFUpload } from '../components/PDFUpload';
import { PDFContent } from '../services/pdfService';

export const CreateQuiz: React.FC = () => {
  const [pdfContent, setPdfContent] = useState<PDFContent | null>(null);

  const handleContentExtracted = (content: PDFContent) => {
    setPdfContent(content);
  };

  return (
    <Container maxWidth="md">
      <Box sx={{ my: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Create New Quiz
        </Typography>

        <Paper sx={{ p: 3, mb: 3 }}>
          <Typography variant="h6" gutterBottom>
            Upload PDF Document
          </Typography>
          <PDFUpload onContentExtracted={handleContentExtracted} />
        </Paper>

        {pdfContent && (
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Extracted Content
            </Typography>
            <Typography variant="subtitle1" gutterBottom>
              Title: {pdfContent.title}
            </Typography>
            <Typography variant="subtitle1" gutterBottom>
              Author: {pdfContent.author}
            </Typography>
            <Typography variant="subtitle1" gutterBottom>
              Subject: {pdfContent.subject}
            </Typography>
            <Typography variant="subtitle1" gutterBottom>
              Pages: {pdfContent.pageCount}
            </Typography>
            <Typography variant="body1" sx={{ mt: 2 }}>
              {pdfContent.text}
            </Typography>
          </Paper>
        )}
      </Box>
    </Container>
  );
}; 