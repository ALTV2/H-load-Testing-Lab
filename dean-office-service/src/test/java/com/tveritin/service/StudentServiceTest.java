package com.tveritin.service;

import com.tveritin.entity.Group;
import com.tveritin.entity.Student;
import com.tveritin.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private UUID studentId;
    private UUID groupId;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();

        student = new Student();
        student.setId(studentId);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setBirthDate(LocalDate.of(2000, 1, 1));
        student.setEmail("john.doe@example.com");
        student.setPhone("+1234567890");
        student.setGroup(new Group());
    }

    @Test
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<Student> students = studentService.getAllStudents();

        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
        assertEquals(student, students.get(0));

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById_Found() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Student foundStudent = studentService.getStudentById(studentId);

        assertNotNull(foundStudent);
        assertEquals(studentId, foundStudent.getId());

        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.getStudentById(studentId));

        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void testAddStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student savedStudent = studentService.addStudent(new Student());

        assertNotNull(savedStudent);
        assertEquals(student.getEmail(), savedStudent.getEmail());

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_Found() {
        Student updatedStudent = new Student();
        updatedStudent.setFirstName("Alice");
        updatedStudent.setLastName("Smith");
        updatedStudent.setBirthDate(LocalDate.of(1999, 5, 15));
        updatedStudent.setEmail("alice.smith@example.com");
        updatedStudent.setPhone("+9876543210");
        updatedStudent.setGroup(new Group());

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        Student result = studentService.updateStudent(studentId, updatedStudent);

        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("alice.smith@example.com", result.getEmail());

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_NotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.updateStudent(studentId, student));

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(0)).save(any(Student.class));
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentRepository).deleteById(studentId);

        studentService.deleteStudent(studentId);

        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    void testGetStudentsByGroupId() {
        when(studentRepository.findByGroupId(groupId)).thenReturn(List.of(student));

        List<Student> students = studentService.getStudentsByGroupId(groupId);

        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
        assertEquals(student, students.get(0));

        verify(studentRepository, times(1)).findByGroupId(groupId);
    }
}
