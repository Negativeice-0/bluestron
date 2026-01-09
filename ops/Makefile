# Load env if present
-include ops/.env.local

.PHONY: setup db-init api-dev ui-dev migrate test typecheck

setup:
	@echo "==> Setup: install frontend deps"
	cd bsui && npm install
	@echo "==> Setup: verify Maven"
	cd bsapi && ./mvnw -v

db-init:
	@echo "==> Initialize Postgres (bsdb)"
	psql -U postgres -h localhost -f ops/db-init.sql

api-build:
    @echo "==> Build bsapi (Spring Boot)"
    cd bsapi && ./mvnw clean package -DskipTests

api-dev:
	@echo "==> Run bsapi (Spring Boot)"
	cd bsapi && ./mvnw spring-boot:run

ui-dev:
	@echo "==> Run bsui (Next.js)"
	cd bsui && npm run dev

migrate:
	@echo "==> Run Java migration runner (Module 1 will implement)"
	cd bsapi && ./mvnw -q -DskipTests exec:java -Dexec.mainClass="co.ke.bluestron.bsapi.migration.Runner"

test:
	@echo "==> Backend tests"
	cd bsapi && ./mvnw test
	@echo "==> Frontend tests"
	cd bsui && npm test || echo "No tests yet"

typecheck:
	@echo "==> Frontend typecheck"
	cd bsui && npm run typecheck