DROP TABLE IF EXISTS cv_soft_skills;
DROP TABLE IF EXISTS soft_skills;
DROP TABLE IF EXISTS certifications;
DROP TABLE IF EXISTS languages;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS experiences;
DROP TABLE IF EXISTS education;
DROP TABLE IF EXISTS job_skills;
DROP TABLE IF EXISTS cv_skills;
DROP TABLE IF EXISTS matches;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS job_descriptions;
DROP TABLE IF EXISTS cvs;
DROP TABLE IF EXISTS users;


-- 1. Users
CREATE TABLE users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100),
    email NVARCHAR(150) UNIQUE NOT NULL,
    password_hash NVARCHAR(MAX) NOT NULL,
    role NVARCHAR(50) CHECK (role IN ('candidate', 'recruiter', 'admin')),
    created_at DATETIME DEFAULT GETDATE()
);

-- 2. CVs
CREATE TABLE cvs (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT FOREIGN KEY REFERENCES users(id) ON DELETE CASCADE,
    title NVARCHAR(150),
    content NVARCHAR(MAX),
    upload_date DATETIME DEFAULT GETDATE(),
    parsed_data NVARCHAR(MAX), -- sau JSON dacă folosești SQL Server 2016+
    embedding_vector NVARCHAR(MAX) -- aici poți salva JSON sau string cu vectorul
);

-- 3. Job Descriptions
CREATE TABLE job_descriptions (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT FOREIGN KEY REFERENCES users(id) ON DELETE SET NULL,
    title NVARCHAR(150),
    description NVARCHAR(MAX),
    requirements NVARCHAR(MAX),
    embedding_vector NVARCHAR(MAX),
    created_at DATETIME DEFAULT GETDATE()
);

-- 4. Matches
CREATE TABLE matches (
    id INT IDENTITY(1,1) PRIMARY KEY,
    cv_id INT FOREIGN KEY REFERENCES cvs(id) ON DELETE CASCADE,
    job_id INT FOREIGN KEY REFERENCES job_descriptions(id) ON DELETE CASCADE,
    match_score FLOAT CHECK (match_score BETWEEN 0 AND 1),
    matched_on DATETIME DEFAULT GETDATE(),
    status NVARCHAR(50) DEFAULT 'pending' CHECK (status IN ('pending', 'accepted', 'rejected'))
);

-- 5. Skills
CREATE TABLE skills (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) UNIQUE NOT NULL
);

-- 6. CV_Skills
CREATE TABLE cv_skills (
    cv_id INT FOREIGN KEY REFERENCES cvs(id) ON DELETE CASCADE,
    skill_id INT FOREIGN KEY REFERENCES skills(id) ON DELETE CASCADE,
    PRIMARY KEY (cv_id, skill_id)
);

-- 7. Job_Skills
CREATE TABLE job_skills (
    job_id INT FOREIGN KEY REFERENCES job_descriptions(id) ON DELETE CASCADE,
    skill_id INT FOREIGN KEY REFERENCES skills(id) ON DELETE CASCADE,
    PRIMARY KEY (job_id, skill_id)
);
-- 8. Education
CREATE TABLE education (
    id INT IDENTITY(1,1) PRIMARY KEY,
    cv_id INT FOREIGN KEY REFERENCES cvs(id) ON DELETE CASCADE,
    institution NVARCHAR(150),
    degree NVARCHAR(100),
    field_of_study NVARCHAR(100),
    start_year INT,
    end_year INT
);

-- 9. Experience
CREATE TABLE experiences (
    id INT IDENTITY(1,1) PRIMARY KEY,
    cv_id INT FOREIGN KEY REFERENCES cvs(id) ON DELETE CASCADE,
    company NVARCHAR(150),
    position NVARCHAR(100),
    start_date DATE,
    end_date DATE,
    description NVARCHAR(MAX)
);

-- 10. Projects
CREATE TABLE projects (
    id INT IDENTITY(1,1) PRIMARY KEY,
    cv_id INT FOREIGN KEY REFERENCES cvs(id) ON DELETE CASCADE,
    name NVARCHAR(150),
    description NVARCHAR(MAX),
    technologies NVARCHAR(300),
    link NVARCHAR(300)
);

-- 11. Languages
CREATE TABLE languages (
    id INT IDENTITY(1,1) PRIMARY KEY,
    cv_id INT FOREIGN KEY REFERENCES cvs(id) ON DELETE CASCADE,
    language NVARCHAR(100),
    level NVARCHAR(50) CHECK (level IN ('beginner', 'intermediate', 'advanced', 'native'))
);

-- 12. Certifications (optional)
CREATE TABLE certifications (
    id INT IDENTITY(1,1) PRIMARY KEY,
    cv_id INT FOREIGN KEY REFERENCES cvs(id) ON DELETE CASCADE,
    name NVARCHAR(150),
    issuer NVARCHAR(150),
    issue_date DATE,
    expiration_date DATE,
    credential_url NVARCHAR(300)
);

-- 13. Soft Skills (optional)
CREATE TABLE soft_skills (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) UNIQUE NOT NULL
);
h

INSERT INTO users (name, email, password_hash, role)
VALUES 
('Andrei Popescu', 'andrei@email.com', 'hash1', 'candidate'),
('Maria Ionescu', 'maria@email.com', 'hash2', 'recruiter');
INSERT INTO cvs (user_id, title, content, parsed_data, embedding_vector)
VALUES 
(1, 'Junior Python Developer CV', 'Lorem ipsum CV content here...', '{"skills": ["Python", "SQL"]}', '[0.11, 0.23, 0.34, ...]');
INSERT INTO job_descriptions (user_id, title, description, requirements, embedding_vector)
VALUES 
(2, 'Backend Developer', 'Looking for a backend dev with Python skills', 'Python, APIs, PostgreSQL', '[0.13, 0.25, 0.31, ...]');
INSERT INTO skills (name)
VALUES ('Python'), ('SQL'), ('Java'), ('APIs');
-- CV-ul lui Andrei are Python și SQL
INSERT INTO cv_skills (cv_id, skill_id)
VALUES 
(1, 1),  -- Python
(1, 2);  -- SQL

-- Jobul are nevoie de Python și APIs
INSERT INTO job_skills (job_id, skill_id)
VALUES 
(1, 1),  -- Python
(1, 4);  -- APIs
INSERT INTO matches (cv_id, job_id, match_score, status)
VALUES 
(1, 1, 0.82, 'pending');

INSERT INTO education (cv_id, institution, degree, field_of_study, start_year, end_year)
VALUES 
(1, 'Universitatea Babeș-Bolyai', 'Bachelor', 'Informatică', 2018, 2021),
(1, 'Liceul Teoretic Avram Iancu', 'High School Diploma', 'Matematică-Informatică', 2014, 2018);
INSERT INTO experiences (cv_id, company, position, start_date, end_date, description)
VALUES 
(1, 'Bitdefender', 'Intern Python Developer', '2021-06-01', '2021-09-01', 'Created Python scripts for internal automation and helped maintain CI/CD pipelines.'),
(1, 'Freelance', 'Junior Web Developer', '2022-02-01', '2022-11-01', 'Built small websites using Flask and PostgreSQL for local clients.');
INSERT INTO projects (cv_id, name, description, technologies, link)
VALUES 
(1, 'CV Matcher AI', 'A tool that uses OpenAI embeddings to match CVs with job descriptions.', 'Python, FastAPI, PostgreSQL', 'https://github.com/andrei/cv-matcher'),
(1, 'Personal Portfolio', 'Web portfolio built using React and hosted on Vercel.', 'React, Tailwind, Vercel', 'https://andrei.dev');
INSERT INTO languages (cv_id, language, level)
VALUES 
(1, 'English', 'advanced'),
(1, 'Romanian', 'native'),
(1, 'German', 'intermediate');
INSERT INTO certifications (cv_id, name, issuer, issue_date, expiration_date, credential_url)
VALUES 
(1, 'Python for Everybody', 'Coursera / University of Michigan', '2021-01-15', NULL, 'https://coursera.org/cert/python123'),
(1, 'SQL Basics', 'SoloLearn', '2020-11-10', NULL, 'https://sololearn.com/cert/sql456');
INSERT INTO soft_skills (name)
VALUES 
('Teamwork'),
('Communication'),
('Problem Solving'),
('Adaptability'),
('Critical Thinking');
INSERT INTO cv_soft_skills (cv_id, skill_id)
VALUES 
(1, 1),  -- Teamwork
(1, 2),  -- Communication
(1, 3);  -- Problem Solving
