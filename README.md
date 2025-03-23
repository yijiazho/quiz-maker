# Quiz Maker

A web application for creating and managing quizzes from PDF documents.

## Features

- PDF document upload and content extraction
- Quiz generation from PDF content
- Interactive quiz interface
- Score tracking and results display

## Prerequisites

- Java 17 or later
- Node.js 18 or later
- Maven 3.9 or later
- Docker Desktop (for Docker setup)

## Development Setup

### Option 1: Local Development

1. Clone the repository:
```bash
git clone https://github.com/yourusername/quiz-maker.git
cd quiz-maker
```

2. Start the backend:
```bash
cd backend
./mvnw spring-boot:run
```

3. Start the frontend:
```bash
cd ../frontend
npm install
npm start
```

4. Access the application:
- Frontend: http://localhost:3000
- Backend: http://localhost:8080

### Option 2: Using Docker

1. Clone the repository:
```bash
git clone https://github.com/yourusername/quiz-maker.git
cd quiz-maker
```

2. Build and start the containers:
```bash
docker-compose up --build
```

3. Access the application:
- Frontend: http://localhost:80
- Backend: http://localhost:8080

### Option 3: Quick Start Script (Windows)

1. Clone the repository:
```bash
git clone https://github.com/yourusername/quiz-maker.git
cd quiz-maker
```

2. Run the start script:
```bash
start-dev.bat
```

This will start both the backend and frontend in development mode.

## Project Structure

```
quiz-maker/
├── backend/                 # Spring Boot backend
│   ├── src/                # Source code
│   ├── pom.xml            # Maven dependencies
│   └── Dockerfile         # Backend container config
├── frontend/              # React frontend
│   ├── src/              # Source code
│   ├── package.json      # Node.js dependencies
│   ├── nginx.conf        # Nginx configuration
│   └── Dockerfile        # Frontend container config
├── docker-compose.yml     # Docker services configuration
└── start-dev.bat         # Windows development start script
```

## Docker Configuration

The application uses Docker Compose to orchestrate two services:

1. Frontend Service:
   - React application served by Nginx
   - Port: 80
   - Handles static file serving and API proxying

2. Backend Service:
   - Spring Boot application
   - Port: 8080
   - Handles PDF processing and quiz generation
   - Persistent volume for PDF storage

## Development Scripts

### start-dev.bat
This Windows batch script automates the development setup:
1. Starts the Spring Boot backend
2. Starts the React development server
3. Opens the application in the default browser

## API Endpoints

- `POST /api/pdf/extract`: Extract content from PDF file
- `POST /api/pdf/extract/page/{pageNumber}`: Extract content from specific PDF page

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
