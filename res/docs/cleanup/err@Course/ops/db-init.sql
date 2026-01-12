-- Create database
CREATE DATABASE bsdb
  WITH OWNER = postgres
  ENCODING = 'UTF8'
  LC_COLLATE = 'en_US.UTF-8'
  LC_CTYPE = 'en_US.UTF-8'
  TEMPLATE = template0;

-- Create dedicated role for API
DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'bsapi_user') THEN
    CREATE ROLE bsapi_user LOGIN PASSWORD 'bsapi_password';
  END IF;
END
$$;

-- Schema per bounded context (created later by migration runner, but we can pre-create)
CREATE SCHEMA IF NOT EXISTS training AUTHORIZATION bsapi_user;
CREATE SCHEMA IF NOT EXISTS services AUTHORIZATION bsapi_user;
CREATE SCHEMA IF NOT EXISTS blog AUTHORIZATION bsapi_user;

-- Grant privileges
GRANT USAGE ON SCHEMA training TO bsapi_user;
GRANT USAGE ON SCHEMA services TO bsapi_user;
GRANT USAGE ON SCHEMA blog TO bsapi_user;

-- Default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA training
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO bsapi_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA services
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO bsapi_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA blog
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO bsapi_user;

-- Grant sequence usage for IDs
ALTER DEFAULT PRIVILEGES IN SCHEMA training
  GRANT USAGE, SELECT ON SEQUENCES TO bsapi_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA services
  GRANT USAGE, SELECT ON SEQUENCES TO bsapi_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA blog
  GRANT USAGE, SELECT ON SEQUENCES TO bsapi_user;
