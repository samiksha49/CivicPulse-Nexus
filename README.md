
# CivicPulse Nexus — Milestone 1

Smart Governance Platform for Administrative Operations with Citizen Service Assistance.

## Milestone 1 Scope

- Citizen registration with server-side validation and duplicate checks.
- Grievance registration, citizen verification, assignment and status workflow.
- SLA deadline calculation, scheduled escalation (up to level 3), audit history and dashboard.
- React dashboard, citizen registration and grievance management screens.

## Technology Stack

- Java 25
- Spring Boot 4.x
- Spring Data JPA
- PostgreSQL
- React 19
- Vite

## Requirements

- JDK 25
- Maven 3.9+
- Node.js 24 LTS (Node 22+ should also work)
- PostgreSQL 17/18 and pgAdmin
- VS Code (Java and Spring extensions recommended)

## 1. Database Setup

1. Open pgAdmin.
2. Select the `postgres` database.
3. Open Query Tool.
4. Run `database/setup.sql`.

If the user or databases already exist, skip the existing creation statements.

Database credentials are configured using environment variables:

- `DB_USERNAME`
- `DB_PASSWORD`

Set these variables before starting the backend services.

## 2. Start Citizen Service (Terminal 1)

Open Windows PowerShell:

```powershell
cd citizen-service

$env:DB_USERNAME="civic_user"
$env:DB_PASSWORD="your_actual_database_password"

mvn spring-boot:run
```

Runs at: http://localhost:8081

Swagger: http://localhost:8081/swagger-ui.html

## 3. Start Grievance Service (Terminal 2)

Open another PowerShell terminal:

```powershell
cd grievance-service

$env:DB_USERNAME="civic_user"
$env:DB_PASSWORD="your_actual_database_password"

mvn spring-boot:run
```

Runs at: http://localhost:8082

Swagger: http://localhost:8082/swagger-ui.html

## 4. Start React Frontend (Terminal 3)

Open another terminal:

```powershell
cd frontend

npm install
npm run dev
```

Open: http://localhost:5173

## API Overview

### Citizen Service

- `POST /api/citizens` — Register citizen
- `GET /api/citizens` — List/search citizens (`?name=`, `?ward=`)
- `GET /api/citizens/{id}` — Get citizen
- `GET /actuator/health` — Health check

### Grievance Service

- `POST /api/grievances` — Register grievance
- `GET /api/grievances` — List grievances (`?status=`)
- `GET /api/grievances/{id}` — Get grievance details
- `GET /api/grievances/{id}/history` — Audit timeline
- `GET /api/grievances/meta` — Categories, severities and departments
- `POST /api/grievances/{id}/assign` — Assign officer/department
- `PATCH /api/grievances/{id}/status` — Change status
- `POST /api/grievances/{id}/escalate` — Manual escalation
- `POST /api/grievances/{id}/simulate-breach` — Demo-only SLA breach
- `GET /api/dashboard/summary` — Dashboard summary

## Demo Sequence for Mentor

1. Register a citizen from the Citizens screen.
2. Register a grievance for that citizen.
3. Assign an officer and department.
4. Move it to IN_PROGRESS.
5. Resolve it with at least 10 characters of resolution notes.
6. Show the audit timeline and dashboard SLA metrics.
7. For escalation demonstration, use Simulate SLA Breach and wait for the scheduler (default interval 60 seconds), or use the manual Escalate action.

## Important Notes

- This is a development/demo implementation. `ddl-auto: update` is not a production migration strategy.
- Aadhaar is masked in API responses. Use dummy data in demonstrations; do not enter real citizen Aadhaar details.
- The SLA scheduler checks every 60 seconds by default.
- Set `civic.escalation.check-interval-ms` in the Grievance Service configuration for a faster local demo.
- Milestones 2–4 (certificates/permits, welfare/budget and analytics) are not included in Milestone 1.
- Never commit real passwords, API keys, private citizen data or database backups to GitHub.
