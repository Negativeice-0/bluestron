Looking at your error, the issue is that your `bsapi_user` doesn't have permission to access the `public` schema. When you create a database, PostgreSQL automatically creates a `public` schema, and you need to grant access to it explicitly.

Here are the additional permissions you should grant:

## PSQL Commands to Run Manually

First, connect to your database and run these commands. **NO NEED to delete the database and re-run the init file**. Just run these commands in your terminal:

```bash
# Connect to your PostgreSQL instance as a superuser (like postgres)
psql -U postgres -d bsdb
```

Once connected to the `bsdb` database, run these SQL commands:

```sql
-- Grant usage on public schema to bsapi_user
GRANT USAGE ON SCHEMA public TO bsapi_user;

-- Grant privileges on existing tables in public schema (if any exist)
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO bsapi_user;

-- Grant privileges on existing sequences in public schema
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO bsapi_user;

-- Set default privileges for future objects in public schema
ALTER DEFAULT PRIVILEGES IN SCHEMA public 
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO bsapi_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA public 
  GRANT USAGE, SELECT ON SEQUENCES TO bsapi_user;
```

## Updated Init File for Future Reference

For your future reference, here's what your init file should include to prevent this error:

```sql
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

-- Revoke default public schema privileges from PUBLIC (security best practice)
REVOKE ALL ON SCHEMA public FROM PUBLIC;

-- Grant connect to database
GRANT CONNECT ON DATABASE bsdb TO bsapi_user;

-- Schema per bounded context
CREATE SCHEMA IF NOT EXISTS training AUTHORIZATION bsapi_user;
CREATE SCHEMA IF NOT EXISTS services AUTHORIZATION bsapi_user;
CREATE SCHEMA IF NOT EXISTS blog AUTHORIZATION bsapi_user;

-- Grant privileges on all schemas including public
GRANT USAGE ON SCHEMA public TO bsapi_user;
GRANT USAGE ON SCHEMA training TO bsapi_user;
GRANT USAGE ON SCHEMA services TO bsapi_user;
GRANT USAGE ON SCHEMA blog TO bsapi_user;

-- Default privileges for future tables in all schemas
DO $$
DECLARE
  schema_name text;
BEGIN
  FOR schema_name IN 
    SELECT nspname FROM pg_namespace 
    WHERE nspname IN ('public', 'training', 'services', 'blog')
  LOOP
    EXECUTE format(
      'ALTER DEFAULT PRIVILEGES IN SCHEMA %I 
       GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO bsapi_user',
      schema_name
    );
    
    EXECUTE format(
      'ALTER DEFAULT PRIVILEGES IN SCHEMA %I 
       GRANT USAGE, SELECT ON SEQUENCES TO bsapi_user',
      schema_name
    );
  END LOOP;
END
$$;
```

## One-Liner Command Alternative

If you want to run all the fixes in one go from your terminal (not inside psql), you can use:

```bash
# Try this comprehensive fix
psql -U postgres -d bsdb -c "
-- Drop and recreate the public schema with proper ownership
DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;
GRANT CREATE, USAGE ON SCHEMA public TO bsapi_user;
GRANT CREATE, USAGE ON SCHEMA public TO PUBLIC;
ALTER SCHEMA public OWNER TO postgres;

-- Set default privileges
ALTER DEFAULT PRIVILEGES FOR USER postgres IN SCHEMA public 
  GRANT ALL PRIVILEGES ON TABLES TO bsapi_user;
  
ALTER DEFAULT PRIVILEGES FOR USER postgres IN SCHEMA public 
  GRANT ALL PRIVILEGES ON SEQUENCES TO bsapi_user;
  
ALTER DEFAULT PRIVILEGES FOR USER bsapi_user IN SCHEMA public 
  GRANT ALL PRIVILEGES ON TABLES TO bsapi_user;
  
ALTER DEFAULT PRIVILEGES FOR USER bsapi_user IN SCHEMA public 
  GRANT ALL PRIVILEGES ON SEQUENCES TO bsapi_user;
"
```

**Success**: After running these commands, your `bsapi_user` should have the necessary permissions to work with all schemas including `public`, and the error should be resolved.