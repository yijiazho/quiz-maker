import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { ThemeProvider, CssBaseline } from '@mui/material';
import theme from './theme';

// Placeholder components - these will be implemented later
const Home = () => <div>Home Page</div>;
const Login = () => <div>Login Page</div>;
const Register = () => <div>Register Page</div>;
const QuizList = () => <div>Quiz List</div>;
const CreateQuiz = () => <div>Create Quiz</div>;

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/quizzes" element={<QuizList />} />
        <Route path="/quizzes/create" element={<CreateQuiz />} />
      </Routes>
    </ThemeProvider>
  );
}

export default App; 