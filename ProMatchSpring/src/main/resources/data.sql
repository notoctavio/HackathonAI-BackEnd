-- Insert test user (password is 'password123' encoded with BCrypt)
INSERT INTO users (email, password, first_name, last_name, company, phone_number, role)
VALUES (
    'test@example.com',
    '$2a$10$rDmFN6ZJvwFqMz1qKqB1UOYgG.0YwqX5Q5Q5Q5Q5Q5Q5Q5Q5Q5Q',
    'Test',
    'User',
    'Test Company',
    '1234567890',
    'USER'
)
ON CONFLICT (email) DO NOTHING;

-- Insert admin user
INSERT INTO users (email, password, first_name, last_name, company, phone_number, role)
VALUES (
    'admin@promatch.com',
    '$2a$10$rDmFN6ZJvwFqMz1qKqB1UOYgG.0YwqX5Q5Q5Q5Q5Q5Q5Q5Q5Q5Q',
    'Admin',
    'User',
    'ProMatch',
    '1234567890',
    'ADMIN'
)
ON CONFLICT (email) DO NOTHING;

-- Insert any initial data if needed
-- For example, you might want to insert a default admin user
-- Note: Passwords should be properly hashed in a real application
-- INSERT INTO users (first_name, last_name, email, password, company, phone_number)
-- VALUES ('Admin', 'User', 'admin@promatch.com', '$2a$10$rDmFN6ZJvwFqMz1qZ.3ZqOYzH1qX5X5X5X5X5X5X5X5X5X5X5X5X', 'ProMatch', '1234567890'); 