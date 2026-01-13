migrate-up:
	psql -U bsdbu -d bsdb -f migrations/001_create_categories_table.sql

run-backend:
	./mvnw spring-boot:run

run-frontend:
	npm run dev