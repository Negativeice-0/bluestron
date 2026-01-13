-- Project Management / Monitoring & Evaluation
INSERT INTO courses (category_id, title, slug, description, duration_hours, price, is_featured)
VALUES
(1, 'Fundamentals of Project Management with Microsoft Project', 'fundamentals-project-management', 'Learn the essentials of project planning and execution using MS Project.', 40, 500.00, TRUE),
(1, 'Grant Proposal Writing and Report Development', 'grant-proposal-writing', 'Master donor proposal writing and reporting for development projects.', 32, 450.00, FALSE),
(1, 'USAID Rules & Regulations - Grant and Cooperative Agreements', 'usaid-rules-regulations', 'Navigate USAID compliance frameworks for grants and cooperative agreements.', 24, 600.00, TRUE),
(1, 'Impact Evaluation for Evidence-Based Development', 'impact-evaluation-development', 'Design and implement rigorous impact evaluations for development programs.', 36, 550.00, FALSE);

-- Data Management & Analysis
INSERT INTO courses (category_id, title, slug, description, duration_hours, price, is_featured)
VALUES
(2, 'Advanced Data Management & Visualization using Excel and Power BI', 'excel-powerbi-data-visualization', 'Harness Excel and Power BI for advanced analytics and dashboards.', 30, 400.00, TRUE),
(2, 'Data Analysis using SPSS', 'data-analysis-spss', 'Perform statistical analysis and reporting using SPSS.', 28, 350.00, FALSE),
(2, 'Data Management Using R-Software', 'data-management-r', 'Learn R for data wrangling, visualization, and reproducible analysis.', 40, 500.00, TRUE),
(2, 'Cybersecurity Vulnerability Assessment & Penetration Testing (VAPT)', 'cybersecurity-vapt', 'Hands-on training in vulnerability assessment and penetration testing.', 40, 700.00, FALSE);

-- GIS & IT / Software
INSERT INTO courses (category_id, title, slug, description, duration_hours, price, is_featured)
VALUES
(3, 'GIS Mapping and Spatial Data Analysis', 'gis-mapping-spatial-analysis', 'Apply GIS tools for monitoring and evaluation projects.', 36, 600.00, TRUE),
(3, 'Mobile Data Collection Using EpiCollect5', 'mobile-data-epicollect5', 'Collect and manage field data using mobile applications.', 24, 300.00, FALSE);

-- Management & Administration
INSERT INTO courses (category_id, title, slug, description, duration_hours, price, is_featured)
VALUES
(4, 'Training on Human Resource Planning & Succession Management', 'hr-planning-succession', 'Develop HR strategies for sustainable organizational growth.', 32, 450.00, FALSE),
(4, 'Mastering the Art of Communication and Influence for Managers', 'communication-influence-managers', 'Enhance communication and influence skills for effective leadership.', 20, 400.00, TRUE),
(4, 'Beyond Customer Service', 'beyond-customer-service', 'Deliver exceptional customer experiences and build loyalty.', 16, 250.00, FALSE);

-- Climate Change / Environment
INSERT INTO courses (category_id, title, slug, description, duration_hours, price, is_featured)
VALUES
(5, 'Training on Climate Finance and Stakeholder Engagement', 'climate-finance-stakeholder', 'Understand climate finance mechanisms and engage stakeholders effectively.', 28, 500.00, TRUE),
(5, 'Training on Environmental and Social Safeguards', 'environmental-social-safeguards', 'Implement safeguards for sustainable development projects.', 30, 450.00, FALSE),
(5, 'Training on ESG and Climate', 'esg-climate-training', 'Integrate ESG principles into climate change adaptation programs.', 36, 600.00, TRUE);
