Nota Bene'

The errors only appeared beacuse the init script which only runs once was not perfect. The below implementation would have avoided the errors to begin with.

# root/ops/db-init.sql

-- Create database
CREATE DATABASE bsdb
  WITH OWNER = postgres
  ENCODING = 'UTF8'
  LC_COLLATE = 'en_US.UTF-8'
  LC_CTYPE = 'en_US.UTF-8'
  TEMPLATE = template0;

-- Connect to the new database (this needs to be run separately or in a script)
\c bsdb

-- Create dedicated role for API
DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'bsapi_user') THEN
    CREATE ROLE bsapi_user LOGIN PASSWORD 'bsapi_password';
  END IF;
END
$$;

-- Grant connect permission
GRANT CONNECT ON DATABASE bsdb TO bsapi_user;

-- Set up public schema properly
-- Revoke default privileges from PUBLIC for security
REVOKE CREATE ON SCHEMA public FROM PUBLIC;

-- Grant ownership of public schema to postgres
ALTER SCHEMA public OWNER TO postgres;

-- Grant necessary permissions to bsapi_user on public schema
GRANT CREATE, USAGE ON SCHEMA public TO bsapi_user;

-- Schema per bounded context
CREATE SCHEMA IF NOT EXISTS training AUTHORIZATION bsapi_user;
CREATE SCHEMA IF NOT EXISTS services AUTHORIZATION bsapi_user;
CREATE SCHEMA IF NOT EXISTS blog AUTHORIZATION bsapi_user;

-- Grant privileges on all schemas
GRANT USAGE ON SCHEMA training TO bsapi_user;
GRANT USAGE ON SCHEMA services TO bsapi_user;
GRANT USAGE ON SCHEMA blog TO bsapi_user;

-- Set default privileges for all schemas including public
DO $$
DECLARE
  schema_name text;
BEGIN
  FOR schema_name IN 
    SELECT unnest(ARRAY['public', 'training', 'services', 'blog'])
  LOOP
    -- For objects created by postgres
    EXECUTE format(
      'ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA %I 
       GRANT ALL PRIVILEGES ON TABLES TO bsapi_user',
      schema_name
    );
    
    EXECUTE format(
      'ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA %I 
       GRANT ALL PRIVILEGES ON SEQUENCES TO bsapi_user',
      schema_name
    );
    
    -- For objects created by bsapi_user itself
    EXECUTE format(
      'ALTER DEFAULT PRIVILEGES FOR ROLE bsapi_user IN SCHEMA %I 
       GRANT ALL PRIVILEGES ON TABLES TO bsapi_user',
      schema_name
    );
    
    EXECUTE format(
      'ALTER DEFAULT PRIVILEGES FOR ROLE bsapi_user IN SCHEMA %I 
       GRANT ALL PRIVILEGES ON SEQUENCES TO bsapi_user',
      schema_name
    );
  END LOOP;
END
$$;

-- Grant existing object privileges
DO $$
DECLARE
  schema_name text;
BEGIN
  FOR schema_name IN 
    SELECT unnest(ARRAY['public', 'training', 'services', 'blog'])
  LOOP
    EXECUTE format(
      'GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA %I TO bsapi_user',
      schema_name
    );
    
    EXECUTE format(
      'GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA %I TO bsapi_user',
      schema_name
    );
    
    EXECUTE format(
      'GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA %I TO bsapi_user',
      schema_name
    );
  END LOOP;
END
$$;

run with 
make db-init
db-init:
	@echo "==> Initialize Postgres (bsdb)"
	psql -U postgres -h localhost -f ops/db-init.sql