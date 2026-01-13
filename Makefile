include .env
export

migrate-up:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f migrations/001_create_categories_table.sql

migrate-courses:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f migrations/002_create_courses_table.sql

seed-categories:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f seeds/003_seed_categories_table.sql

seed-courses:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f seeds/004_seed_courses_table.sql

run-bsapi:
	cd bsapi && ./mvnw spring-boot:run

build-bsapi:
	cd bsapi && ./mvnw clean package -DskipTests	

run-bsui:
	cd bsui && npm run dev

# Helper target to test database connection
test-db:
	PGPASSWORD=$(DB_PASSWORD) psql -U $(DB_USER) -d $(DB_NAME) -c "SELECT version();"