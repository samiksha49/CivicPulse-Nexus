# Mentor Demonstration Guide — CivicPulse Nexus (Milestone 1)

## 60-second introduction
"Good morning, ma'am/sir. My project is CivicPulse Nexus, a smart governance platform for municipal administrative operations. Milestone 1 focuses on citizen registration and grievance management. It uses two independent Spring Boot microservices with separate PostgreSQL databases and a React frontend. Citizens can register, submit grievances, and municipal officers can assign, track, resolve and escalate complaints while the system monitors SLA deadlines."

## Architecture explanation
- React + Vite frontend: Dashboard, Citizens and Grievances screens (port 5173).
- Citizen Service: Spring Boot REST API, citizen validation and citizen database (port 8081).
- Grievance Service: grievance workflow, SLA and audit history (port 8082).
- PostgreSQL: `civic_citizen_db` belongs to Citizen Service; `civic_grievance_db` belongs to Grievance Service.
- Grievance Service validates a citizen by calling Citizen Service over HTTP. It does not read the citizen database directly.
- Vite proxy forwards `/api/citizens` to 8081 and grievance/dashboard APIs to 8082.

## Live demo order
1. Open Dashboard and explain the citizen, grievance, resolution and SLA cards.
2. Open Citizens. Register a demo citizen with dummy details. Point out generated citizen code and masked Aadhaar.
3. Open Grievances. Register a complaint for that citizen (e.g. Water Supply, High severity).
4. Select the new grievance. Assign an officer and department.
5. Start Work, then resolve with a meaningful resolution note of at least 10 characters.
6. Show the audit timeline, SLA due time and escalation level.
7. For escalation, create another open grievance and click "Simulate SLA Breach". The scheduler checks overdue items every 60 seconds by default. You can also demonstrate manual escalation.
8. Return to Dashboard and refresh the metrics.

## Important concepts to explain
- Microservice: a small independently deployable service that owns its data.
- REST API: HTTP endpoints used by frontend and service-to-service communication.
- JPA/Hibernate: maps Java entities to PostgreSQL tables.
- DTO + Bean Validation: validates input and controls the JSON returned to clients.
- SLA: target time to act on a grievance; overdue open complaints can be escalated.
- Audit history: stores each important action and status change.

## Likely mentor questions
**Why two databases?** Each microservice owns its data, so services remain independent and can be deployed or scaled separately.

**Why does Grievance Service call Citizen Service?** It needs to confirm that the citizen exists. Direct database access would tightly couple the services.

**What is DTO?** A Data Transfer Object defines the data accepted or returned by an API. The response masks Aadhaar instead of exposing the stored value.

**What is SLA?** Service Level Agreement; in this project it is the allowed response/resolution time based on category and severity.

**What is escalation?** If an open grievance crosses its SLA deadline, the scheduler raises its escalation level (up to level 3) and records the event.

**What is the audit timeline?** A history of registration, assignment, status changes and escalation, including who performed the action and when.

**What is not included in Milestone 1?** Certificate and permit management, welfare and budget management, and governance analytics are later milestones.

## Before the meeting
- Start PostgreSQL and verify both databases exist.
- Start both backend services and confirm their ports.
- Start the frontend and open http://localhost:5173.
- Use dummy citizen information only; do not demonstrate with real Aadhaar data.
- Keep this guide and the README available for questions.
