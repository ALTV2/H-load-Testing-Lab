CREATE DATABASE DeaneryDB;
USE DeaneryDB;

-- Таблица студентов
CREATE TABLE Students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    group_id INT NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE,
    FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE
);

-- Таблица групп
CREATE TABLE Groups (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    year INT NOT NULL,
    faculty VARCHAR(100) NOT NULL
);

-- Таблица преподавателей
CREATE TABLE Teachers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE
);

-- Таблица дисциплин
CREATE TABLE Subjects (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    teacher_id INT NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES Teachers(id) ON DELETE SET NULL
);

-- Таблица оценок
CREATE TABLE Grades (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    grade INT CHECK (grade BETWEEN 1 AND 10),
    date_received DATE NOT NULL,
    FOREIGN KEY (student_id) REFERENCES Students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES Subjects(id) ON DELETE CASCADE
);

-- Таблица расписания
CREATE TABLE Schedule (
    id INT PRIMARY KEY AUTO_INCREMENT,
    group_id INT NOT NULL,
    subject_id INT NOT NULL,
    teacher_id INT NOT NULL,
    day_of_week ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday') NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES Subjects(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES Teachers(id) ON DELETE CASCADE
);