@echo off
echo CivicPulse Nexus - Milestone 1
echo Start PostgreSQL first, then run the three commands in separate terminals:
echo.
echo 1) cd citizen-service ^&^& mvn spring-boot:run
echo 2) cd grievance-service ^&^& mvn spring-boot:run
echo 3) cd frontend ^&^& npm install ^&^& npm run dev
echo.
echo Open http://localhost:5173
pause
