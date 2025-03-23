import React, { useCallback, useState } from 'react';
import { useDropzone } from 'react-dropzone';
import { pdfService, PDFContent } from '../services/pdfService';
import { Box, Typography, CircularProgress, Alert } from '@mui/material';

interface PDFUploadProps {
  onContentExtracted: (content: PDFContent) => void;
}

export const PDFUpload: React.FC<PDFUploadProps> = ({ onContentExtracted }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const onDrop = useCallback(async (acceptedFiles: File[]) => {
    const file = acceptedFiles[0];
    if (!file) return;

    setIsLoading(true);
    setError(null);

    try {
      const content = await pdfService.extractContent(file);
      onContentExtracted(content);
    } catch (err) {
      setError('Failed to process PDF file. Please try again.');
      console.error('PDF processing error:', err);
    } finally {
      setIsLoading(false);
    }
  }, [onContentExtracted]);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: {
      'application/pdf': ['.pdf']
    },
    maxFiles: 1
  });

  return (
    <Box sx={{ width: '100%', p: 2 }}>
      <Box
        {...getRootProps()}
        sx={{
          border: '2px dashed',
          borderColor: isDragActive ? 'primary.main' : 'grey.300',
          borderRadius: 2,
          p: 3,
          textAlign: 'center',
          cursor: 'pointer',
          '&:hover': {
            borderColor: 'primary.main',
          },
        }}
      >
        <input {...getInputProps()} />
        {isLoading ? (
          <CircularProgress />
        ) : (
          <Typography>
            {isDragActive
              ? 'Drop the PDF file here'
              : 'Drag and drop a PDF file here, or click to select'}
          </Typography>
        )}
      </Box>

      {error && (
        <Alert severity="error" sx={{ mt: 2 }}>
          {error}
        </Alert>
      )}
    </Box>
  );
}; 