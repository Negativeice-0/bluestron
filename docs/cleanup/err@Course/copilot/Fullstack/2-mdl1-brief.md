After pasting in error 1 from errors folder, i received a layout.





Now let’s move into Module 1 — Core Domain (Schemas + OpenAPI endpoints). This is where the PDF requirements become actual entities, migrations, and contracts. We’ll lock the truth layer before touching admin or UI.

🔹 Module 1 — Schemas and OpenAPI Endpoints

Entities and Schemas (Postgres: bsdb)

Training domain

CourseCategory

id SERIAL PRIMARY KEY

slug VARCHAR(100) UNIQUE NOT NULL

name VARCHAR(150) NOT NULL

description TEXT

status ENUM('active','inactive') DEFAULT 'active'

Course

id SERIAL PRIMARY KEY

slug VARCHAR(150) UNIQUE NOT NULL

title VARCHAR(200) NOT NULL

short_description TEXT

full_description TEXT

learning_outcomes TEXT[]

thumbnail_url TEXT

category_id INT REFERENCES CourseCategory(id)

status ENUM('draft','published','archived') DEFAULT 'draft'

Venue

id SERIAL PRIMARY KEY

name VARCHAR(150)

city VARCHAR(100)

address TEXT

map_url TEXT

CourseInstance (Cohort)

id SERIAL PRIMARY KEY

course_id INT REFERENCES Course(id)

mode ENUM('online','in_person')

start_date DATE

end_date DATE

capacity INT

status ENUM('open','waitlist','closed') DEFAULT 'open'

venue_id INT REFERENCES Venue(id)

Registration

id SERIAL PRIMARY KEY

course_id INT REFERENCES Course(id)

course_instance_id INT REFERENCES CourseInstance(id)

full_name VARCHAR(150)

email VARCHAR(150)

phone VARCHAR(50)

organization VARCHAR(150)

role VARCHAR(100)

preferred_date DATE

payment_option ENUM('online','invoice')

payment_status ENUM('pending','success','failed') DEFAULT 'pending'

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

Services domain

ServiceOffering

id SERIAL PRIMARY KEY

slug VARCHAR(100) UNIQUE NOT NULL

title VARCHAR(200)

description TEXT

category ENUM('research','data_analysis','software')

status ENUM('active','inactive') DEFAULT 'active'

ServiceEnquiry

id SERIAL PRIMARY KEY

service_offering_id INT REFERENCES ServiceOffering(id)

full_name VARCHAR(150)

email VARCHAR(150)

phone VARCHAR(50)

organization VARCHAR(150)

message TEXT

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

Blog domain

BlogPostid SERIAL PRIMARY KEY

slug VARCHAR(150) UNIQUE NOT NULL

title VARCHAR(200)

summary TEXT

markdown TEXT

thumbnail_url TEXT

status ENUM('draft','published','archived') DEFAULT 'draft'

published_at TIMESTAMP

OpenAPI Endpoints (Spring Boot controllers)

Training

GET /api/categories → list categories

POST /api/categories → create category

GET /api/courses → list courses (filters: category, status)

POST /api/courses → create course

GET /api/courses/{slug} → course detail

GET /api/courses/{slug}/instances → list cohorts

POST /api/courses/{slug}/instances → create cohort

POST /api/registrations → register for course/cohort

GET /api/registrations/{id} → registration detail

Services

GET /api/services → list service offerings

POST /api/services → create service offering

GET /api/services/{slug} → service detail

POST /api/services/{slug}/enquiries → submit enquiry

Blog

GET /api/blog → list posts (filters: status)

GET /api/blog/{slug} → post detail

POST /api/blog → create post (markdown content)

Java Migration Runner (local, Maven exec)

We’ll implement migrations as versioned Java classes:



package co.ke.bluestron.bsapi.migration;



import java.sql.Connection;

import java.sql.Statement;



public class V1__InitTrainingSchema implements Migration {

    @Override

    public void up(Connection conn) throws Exception {

        try (Statement st = conn.createStatement()) {

            st.execute("CREATE TABLE IF NOT EXISTS course_category (...);");

            // other tables...

        }

    }



    @Override

    public void down(Connection conn) throws Exception {

        try (Statement st = conn.createStatement()) {

            st.execute("DROP TABLE IF EXISTS registration;");

            st.execute("DROP TABLE IF EXISTS course_instance;");

            st.execute("DROP TABLE IF EXISTS course;");

            st.execute("DROP TABLE IF EXISTS course_category;");

        }

    }

}

Runner entrypoint:



package co.ke.bluestron.bsapi.migration;



import java.sql.Connection;

import java.sql.DriverManager;



public class Runner {

    public static void main(String[] args) throws Exception {

        String url = System.getenv("BSAPI_DB_URL");

        String user = System.getenv("BSAPI_DB_USER");

        String pass = System.getenv("BSAPI_DB_PASSWORD");



        try (Connection conn = DriverManager.getConnection(url, user, pass)) {

            new V1__InitTrainingSchema().up(conn);

            // future migrations...

        }

    }

}

Run via Makefile:



migrate:

    cd bsapi && ./mvnw -q -DskipTests exec:java -Dexec.mainClass="co.ke.bluestron.bsapi.migration.Runner"

Verification checkpoints for Module 1

DB: Tables created in bsdb with correct constraints.

API: Endpoints exposed in /swagger-ui/index.html.

Contracts: TypeScript clients generated for bsui.

Validation: DTOs + error envelopes in Spring Boot.

Seed data: Insert sample categories/courses for testing.

👉 Next step: I’ll map DTOs + OpenAPI schema definitions for each entity (so frontend can generate clients cleanly). Do you want me to show the exact JSON schema fragments for OpenAPI (e.g. Course, Registration) now, or keep it high-level until migrations are verified?

