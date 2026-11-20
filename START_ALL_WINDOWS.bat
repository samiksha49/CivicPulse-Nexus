@echo off
set ROOT=%~dp0
start "Citizen Service - 8081" cmd /k "cd /d %ROOT%citizen-service && mvn spring-boot:run"
start "Grievance Service - 8082" cmd /k "cd /d %ROOT%grievance-service && mvn spring-boot:run"
start "React Frontend - 5173" cmd /k "cd /d %ROOT%frontend && npm install && npm run dev"
echo Started three terminals. Ensure PostgreSQL is running and the databases are created.
pause
