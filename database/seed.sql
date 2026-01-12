TRUNCATE TABLE registrations, courses, categories RESTART IDENTITY;

-- Categories
INSERT INTO categories (name, slug, display_order) VALUES
('Project Management & M&E', 'project-management-me', 1),
('Data Management & Analysis', 'data-management-analysis', 2),
('GIS & IT', 'gis-it', 3),
('Management & HR', 'management-hr', 4),
('Climate Change', 'climate-change', 5);

-- Courses (sample from PDF)
INSERT INTO courses (category_id, title, slug, description, short_description, duration, mode, price, featured) VALUES
(1, 'Fundamentals of Project Management with Microsoft Project', 'fundamentals-project-management', 
'Comprehensive training on project management fundamentals.', 'Master project management with MS Project.',
'5 days', 'IN_PERSON', 500.00, TRUE),

(1, 'Project Management for Development Professionals', 'project-management-development',
'Project management tailored for development sector.', 'Essential PM skills for development work.',
'4 days', 'HYBRID', 450.00, TRUE),

(2, 'Advanced Data Management with Excel and Power BI', 'advanced-data-excel-powerbi',
'Data analysis and visualization with Excel and Power BI.', 'Transform data into insights.',
'4 days', 'ONLINE', 400.00, TRUE),

(2, 'Data Analysis using SPSS', 'data-analysis-spss',
'Statistical analysis using SPSS software.', 'Learn professional statistical analysis.',
'3 days', 'HYBRID', 350.00, FALSE),

(3, 'GIS in Monitoring & Evaluation', 'gis-monitoring-evaluation',
'Geographic Information Systems for M&E.', 'Spatial analysis for monitoring and evaluation.',
'3 days', 'IN_PERSON', 420.00, TRUE);

-- Sample registration
INSERT INTO registrations (course_id, full_name, email, phone, organization, payment_option) VALUES
(1, 'John Doe', 'john@example.com', '+254712345678', 'NGO Ltd', 'INVOICE');

-- Add admin user (password: Admin123!)
-- BCrypt hash for 'Admin123!' the one below is fake -- instead register 
-- a new admin with curl to test registration)
-- bycrypt encyptor "passwordconfig.java" did it for me automatically as it does.
-- password still 'Admin123!; but email that works is 'newadmin@bluestron.co.ke'
INSERT INTO users (email, password, full_name, role) VALUES
('admin@bluestron.co.ke', '$2a$12$q7v.BhOe5E3z7b5W8pQYQO5v5T6n7v8w9x0y1z2A3B4C5D6E7F8G9H0I1J', 'Bluestron Admin', 'ADMIN');