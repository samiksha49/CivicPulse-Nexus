
-- Run as PostgreSQL superuser in the postgres database.
-- Replace YOUR_SECURE_PASSWORD with your own database password
-- when setting up a new environment.

CREATE USER civic_user WITH PASSWORD 'YOUR_SECURE_PASSWORD';

CREATE DATABASE civic_citizen_db OWNER civic_user;
CREATE DATABASE civic_grievance_db OWNER civic_user;

GRANT ALL PRIVILEGES ON DATABASE civic_citizen_db TO civic_user;
GRANT ALL PRIVILEGES ON DATABASE civic_grievance_db TO civic_user;
