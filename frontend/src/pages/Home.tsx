import React from 'react';
import { Box, Container, Typography, Button, Paper } from '@mui/material';
import { PDFUpload } from '../components/PDFUpload';
import { PDFContent } from '../services/pdfService';

const Home: React.FC = () => {
  const handleContentExtracted = (content: PDFContent) => {
    console.log('PDF content extracted:', content);
    // TODO: Handle the extracted content (e.g., navigate to quiz creation)
  };

  return (
    <Container maxWidth="md">
      <Box sx={{ mt: 8, mb: 4 }}>
        <Typography variant="h3" component="h1" gutterBottom align="center">
          Quiz Maker
        </Typography>
        <Typography variant="h5" component="h2" gutterBottom align="center" color="text.secondary">
          Create quizzes from your PDF documents
        </Typography>
      </Box>

      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Typography variant="h6" gutterBottom>
          Upload PDF Document
        </Typography>
        <Typography variant="body1" color="text.secondary" paragraph>
          Upload a PDF document to extract its content and create a quiz. The system will analyze the content
          and help you generate relevant questions.
        </Typography>
        <PDFUpload onContentExtracted={handleContentExtracted} />
      </Paper>
    </Container>
  );
};

export default Home; 