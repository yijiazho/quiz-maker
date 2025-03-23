@echo off
echo Starting Quiz Maker Development Environment...

:: Start Backend Server
echo Starting Backend Server...
start "Backend Server" cmd /k "cd backend && mvn spring-boot:run"

:: Wait for backend to start
timeout /t 5 /nobreak

:: Start Frontend Server
echo Starting Frontend Server...
start "Frontend Server" cmd /k "cd frontend && npm start"

echo.
echo Development servers are starting...
echo Backend will be available at: http://localhost:8080
echo Frontend will be available at: http://localhost:3000
echo.
echo Press any key to close all servers...
pause > nul

:: Kill all running servers
taskkill /F /IM java.exe /T
taskkill /F /IM node.exe /T 