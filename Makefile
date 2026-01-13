include .env
export

migrate-up:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f migrations/001_create_categories_table.sql

run-bsapi:
	cd bsapi && ./mvnw spring-boot:run

build-bsapi:
	cd bsapi && ./mvnw clean package -DskipTests	

run-bsui:
	cd bsui && npm run dev

# Helper target to test database connection
test-db:
	PGPASSWORD=$(DB_PASSWORD) psql -U $(DB_USER) -d $(DB_NAME) -c "SELECT version();"