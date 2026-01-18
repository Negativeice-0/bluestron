include .env
export

migrate-up:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f migrations/001_create_categories_table.sql

migrate-courses:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f migrations/002_create_courses_table.sql

migrate-registrations:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f migrations/003_create_registrations_table.sql

migrate-users:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f migrations/004_create_users_table.sql

migrate-media:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f migrations/005_create_media_table.sql

migrate-services:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f migrations/006_create_services_table.sql

migrate-blog:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f migrations/007_create_blog_posts_table.sql

migrate-contacts:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f migrations/008_create_contacts_and_testimonials.sql

migrate-admin:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f migrations/009_create_admin_settings.sql

seed-categories:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f seeds/001_seed_categories_table.sql

seed-courses:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f seeds/002_seed_courses_table.sql

seed-registrations:
	PGPASSWORD='$(DB_PASSWORD)' psql -U $(DB_USER) -d $(DB_NAME) -f seeds/003_seed_registrations.sql


run-bsapi:
	cd bsapi && ./mvnw clean spring-boot:run


build-bsapi:
	cd bsapi && ./mvnw clean package -DskipTests	

run-bsui:
	cd bsui && npm run dev

# Helper target to test database connection
test-db:
	PGPASSWORD=$(DB_PASSWORD) psql -U $(DB_USER) -d $(DB_NAME) -c "SELECT version();"