INSERT INTO registrations (course_id, full_name, email, phone, status)
VALUES 
  (1, 'Alice Johnson', 'alice@example.com', '555-1111', 'CONFIRMED'),
  (1, 'Bob Smith', 'bob@example.com', '555-2222', 'PENDING'),
  (2, 'Carol White', 'carol@example.com', '555-3333', 'CANCELLED')
ON CONFLICT DO NOTHING;
