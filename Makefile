.PHONY: help db-create db-drop db-schema db-seed db-reset db-psql \
        be-build be-run be-test be-curl-courses be-curl-register \
        fe-install fe-dev fe-build fe-start setup-all


# Colors for output
GREEN := \033[0;32m
RED := \033[0;31m
NC := \033[0m

help:
	@echo "Bluestron MVP - Simple Training Business Platform"
	@echo ""
	@echo "Database Commands:"
	@echo "  make db-create    - Create database"
	@echo "  make db-drop      - Drop database"
	@echo "  make db-schema    - Apply schema"
	@echo "  make db-seed      - Load seed data"
	@echo "  make db-reset     - Reset database (drop, create, schema, seed)"
	@echo "  make db-psql      - Connect to database"
	@echo ""
	@echo "Backend Commands:"
	@echo "  make be-build     - Build Spring Boot app"
	@echo "  make be-run       - Run backend on port 8080"
	@echo "  make be-test      - Run tests"
	@echo "  make be-curl-courses   - Test courses API"
	@echo "  make be-curl-register  - Test registration API"
	@echo ""
	@echo "Frontend Commands:"
	@echo "  make fe-install   - Install Node dependencies"
	@echo "  make fe-dev       - Run dev server on port 3000"
	@echo "  make fe-build     - Build for production"
	@echo "  make fe-start     - Start production server"
	@echo ""
	@echo "Setup All:"
	@echo "  make setup-all    - Full setup (db + backend + frontend)"
	@echo ""

# ========== DATABASE COMMANDS ==========
# Load environment variables (db only)
ifneq (,$(wildcard .env.db))
    include .env.db
    export
endif
# --- Database Creation ---
db-create:
	@echo "$(GREEN)Creating database $(DB_NAME)...$(NC)"
	@psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_SUPERUSER) -d postgres -tc \
		"SELECT 1 FROM pg_database WHERE datname = '$(DB_NAME)'" | grep -q 1 && \
		echo "$(YELLOW)Database already exists$(NC)" || \
		(psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_SUPERUSER) -d postgres -c \
			"CREATE DATABASE $(DB_NAME) OWNER $(DB_SUPERUSER);" && \
		psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_SUPERUSER) -d postgres -c \
			"CREATE USER $(DB_USER) WITH PASSWORD '$(DB_PASSWORD)';" && \
		echo "$(GREEN)✓ Database and user created successfully$(NC)")

# --- Privilege Assignment ---
db-grant:
	@echo "$(GREEN)Granting privileges to $(DB_USER) on $(DB_NAME)...$(NC)"
	@psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_SUPERUSER) -d $(DB_NAME) -c \
		"GRANT CONNECT ON DATABASE $(DB_NAME) TO $(DB_USER); \
		GRANT USAGE ON SCHEMA public TO $(DB_USER); \
		GRANT CREATE ON SCHEMA public TO $(DB_USER); \
		ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO $(DB_USER); \
		ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE, SELECT ON SEQUENCES TO $(DB_USER);"
	@echo "$(GREEN)✓ Privileges granted successfully$(NC)"

db-drop:
	@echo "$(RED)Dropping database $(DB_NAME)...$(NC)"
	@dropdb -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) --if-exists $(DB_NAME) 2>/dev/null && \
		echo "Database dropped" || \
		echo "Error dropping database"

db-schema:
	@echo "$(GREEN)Applying database schema...$(NC)"
	@PGPASSWORD='$(DB_PASSWORD)' psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME) -f database/schema.sql

db-seed:
	@echo "$(GREEN)Loading seed data...$(NC)"
	@PGPASSWORD='$(DB_PASSWORD)' psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME) -f database/seed.sql
             #as aboveabove if pass has specials like ! use '' on db variable
db-reset: db-drop db-create db-schema db-seed
	@echo "$(GREEN)Database reset complete!$(NC)"

db-psql:
	@echo "$(GREEN)Connecting to database...$(NC)"
	@PGPASSWORD='$(DB_PASSWORD)' psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME)

# ========== BACKEND COMMANDS ==========
be-build:
	@echo "$(GREEN)Building Spring Boot application...$(NC)"
	@cd bsapi && ./mvnw clean package -DskipTests

be-run:
	@echo "$(GREEN)Starting Spring Boot on port 8080...$(NC)"
	@cd bsapi && ./mvnw spring-boot:run

be-test:
	@echo "$(GREEN)Running backend tests...$(NC)"
	@cd bsapi && ./mvnw test

be-curl-courses:
	@echo "$(GREEN)Testing courses endpoint...$(NC)"
	@curl -s http://localhost:8080/api/courses | python -m json.tool || \
		echo "Server might not be running"

be-curl-register:
	@echo "$(GREEN)Testing registration endpoint...$(NC)"
	@curl -X POST http://localhost:8080/api/registrations \
		-H "Content-Type: application/json" \
		-d '{"courseId":1,"fullName":"Test User","email":"test@x.com","phone":"0700000000"}' || \
		echo "Server might not be running"

# ========== FRONTEND COMMANDS ==========
fe-install:
	@echo "$(GREEN)Installing frontend dependencies...$(NC)"
	@cd bsui && npm install

fe-dev:
	@echo "$(GREEN)Starting Next.js dev server on port 3000...$(NC)"
	@cd bsui && npm run dev

fe-build:
	@echo "$(GREEN)Building frontend for production...$(NC)"
	@cd bsui && npm run build

fe-start:
	@echo "$(GREEN)Starting production frontend...$(NC)"
	@cd bsui && npm start

# ========== SETUP ALL ==========
setup-all: db-reset be-build fe-install
	@echo ""
	@echo "$(GREEN)========================================$(NC)"
	@echo "$(GREEN)SETUP COMPLETE!$(NC)"
	@echo "$(GREEN)========================================$(NC)"
	@echo ""
	@echo "To start the application:"
	@echo "1. Terminal 1: make be-run"
	@echo "2. Terminal 2: make fe-dev"
	@echo ""
	@echo "Access:"
	@echo "  Frontend: http://localhost:3000"
	@echo "  Backend API: http://localhost:8080/api/courses"
	@echo "  Health check: http://localhost:8080/actuator/health"
	@echo ""
	@echo "Default admin login (for actuator endpoints):"
	@echo "  Username: admin"
	@echo "  Password: lsetga"

# ========== VERIFICATION ==========
verify:
	@echo "$(GREEN)Verifying setup...$(NC)"
	@echo "1. Checking PostgreSQL..."
	@which psql >/dev/null && echo "✓ PostgreSQL installed" || echo "✗ PostgreSQL not found"
	@echo "2. Checking Node.js..."
	@which node >/dev/null && echo "✓ Node.js installed" || echo "✗ Node.js not found"
	@echo "3. Checking Java..."
	@which java >/dev/null && echo "✓ Java installed" || echo "✗ Java not found"
	@echo "4. Checking database connection..."
	@PGPASSWORD=$(DB_PASSWORD) psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME) -c "SELECT 1" >/dev/null 2>&1 && \
		echo "✓ Database connected" || echo "✗ Database connection failed"

# ========== CLEANUP ==========
clean:
	@echo "$(GREEN)Cleaning up...$(NC)"
	@cd bsapi && ./mvnw clean
	@cd bsui && rm -rf .next node_modules
	@echo "Clean complete"