import axios from 'axios';

const API_URL = '/api';

export interface PDFContent {
  text: string;
  pageCount: number;
  title: string;
  author: string;
  subject: string;
}

export const pdfService = {
  async extractContent(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    
    try {
      const response = await axios.post(`${API_URL}/pdf/extract`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      return response.data;
    } catch (error) {
      console.error('Error extracting PDF content:', error);
      throw error;
    }
  },

  async getPageContent(pageNumber: number) {
    try {
      const response = await axios.get(`${API_URL}/pdf/extract/page/${pageNumber}`);
      return response.data;
    } catch (error) {
      console.error('Error getting page content:', error);
      throw error;
    }
  }
}; 