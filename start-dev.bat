@echo off
echo Starting Quiz Maker Development Environment...

:: Start backend
echo Starting Spring Boot backend...
start cmd /k "cd backend && mvn spring-boot:run"

:: Wait for backend to start
timeout /t 10 /nobreak

:: Start frontend
echo Starting React frontend...
start cmd /k "cd frontend && npm install && npm start"

:: Open browser
echo Opening application in browser...
timeout /t 5 /nobreak
start http://localhost:3000

echo Development environment is starting...
echo Backend will be available at http://localhost:8080
echo Frontend will be available at http://localhost:3000 