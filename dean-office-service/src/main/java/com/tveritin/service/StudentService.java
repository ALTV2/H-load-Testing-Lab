package com.tveritin.service;

import com.tveritin.entity.Student;
import com.tveritin.repository.StudentRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(UUID id) {
        return studentRepository.findById(id).orElseThrow();
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(UUID id, Student studentDetails) {
        Student student = getStudentById(id);
        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        // Update other fields as necessary
        return studentRepository.save(student);
    }

    public void deleteStudent(UUID id) {
        studentRepository.deleteById(id);
    }
}