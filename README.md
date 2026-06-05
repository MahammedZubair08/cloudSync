# CloudSync

CloudSync is a simple file sync web application with a React frontend and a Spring Boot backend.

## Overview

- **Backend:** Spring Boot + Java 21 + JPA + Spring Security + JWT + AWS S3
- **Frontend:** React + Vite + React Router + Axios
- **Data store:** PostgreSQL
- **File storage:** AWS S3

## Features

- User registration and login
- JWT-based authentication
- Protected dashboard access
- File upload to S3
- File listing filtered by logged-in user
- Download files via signed S3 URLs
- Delete uploaded files (S3 object + database record)

## Repository Structure

- `cloudsync-backend/`
  - Spring Boot backend
  - `src/main/java/com/cloudsync/cloudsync/`
    - `controller/` - REST controllers
    - `service/` - upload/download/delete and auth logic
    - `repository/` - JPA repositories
    - `security/` - JWT auth filter and security config
    - `util/` - JWT utilities
    - `entity/` - JPA entities
    - `dto/` - request DTOs
  - `src/main/resources/application-example.properties` - example environment config

- `cloudsync-frontend/`
  - React frontend
  - `src/pages/` - login, register, dashboard pages
  - `src/components/ProtectedRoute.jsx` - route guard
  - `src/api/axios.js` - Axios instance with auth header injection

## Backend Setup

1. Copy the example config:

   ```bash
   cp cloudsync-backend/src/main/resources/application-example.properties cloudsync-backend/src/main/resources/application.properties
   ```

2. Edit `cloudsync-backend/src/main/resources/application.properties` and configure:
   - PostgreSQL connection (`spring.datasource.url`, `username`, `password`)
   - AWS credentials and region
   - `aws.s3.bucket`

3. Build and run the backend:

   ```bash
   cd cloudsync-backend
   ./mvnw spring-boot:run
   ```

## Frontend Setup

1. Install frontend dependencies:

   ```bash
   cd cloudsync-frontend
   npm install
   ```

2. Run the frontend app:

   ```bash
   npm run dev
   ```

3. Open the app in the browser at the URL shown by Vite (usually `http://localhost:5173`).

## API Endpoints

- `POST /api/auth/register` - create a new user
- `POST /api/auth/login` - authenticate and receive a JWT token
- `GET /api/files` - list files uploaded by the authenticated user
- `POST /api/files/upload` - upload a file to S3
- `GET /api/files/download/{id}` - generate a presigned download URL
- `DELETE /api/files/{id}` - delete a file owned by the authenticated user

## Notes

- The frontend stores the JWT token in `localStorage` and sends it on protected API requests.
- The backend validates the token and restricts file access so users only see and delete their own uploads.
- The project currently uses a fixed JWT secret inside `JwtService`; replace it with a secure secret management process for production.

## Development

- Run backend and frontend simultaneously in separate terminals.
- Use the register page to create a new account, then log in.
- The dashboard lets you upload files, view your own uploads, download them, and delete them.
