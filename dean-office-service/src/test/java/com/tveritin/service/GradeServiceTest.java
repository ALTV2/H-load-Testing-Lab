package com.tveritin.service;

import com.tveritin.entity.Grade;
import com.tveritin.entity.Student;
import com.tveritin.entity.Subject;
import com.tveritin.entity.Teacher;
import com.tveritin.repository.GradeRepository;
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
class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private GradeService gradeService;

    private Grade grade;
    private UUID gradeId;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        gradeId = UUID.randomUUID();
        studentId = UUID.randomUUID();

        var student = new Student();
        student.setFirstName("Alexandr");
        student.setFirstName("Tveritin");

        var subject = new Subject();
        subject.setName("Math");
        subject.setTeacher(new Teacher());

        grade = new Grade();
        grade.setId(gradeId);
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setGrade(95);
        grade.setDateReceived(LocalDate.now());
    }

    @Test
    void testGetAllGrades() {
        when(gradeRepository.findAll()).thenReturn(List.of(grade));

        List<Grade> grades = gradeService.getAllGrades();

        assertFalse(grades.isEmpty());
        assertEquals(1, grades.size());
        assertEquals(grade, grades.get(0));

        verify(gradeRepository, times(1)).findAll();
    }

    @Test
    void testGetGradeById_Found() {
        when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));

        Grade foundGrade = gradeService.getGradeById(gradeId);

        assertNotNull(foundGrade);
        assertEquals(gradeId, foundGrade.getId());

        verify(gradeRepository, times(1)).findById(gradeId);
    }

    @Test
    void testGetGradeById_NotFound() {
        when(gradeRepository.findById(gradeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> gradeService.getGradeById(gradeId));
        assertEquals("Grade not found", exception.getMessage());

        verify(gradeRepository, times(1)).findById(gradeId);
    }

    @Test
    void testSaveGrade() {
        when(gradeRepository.save(any(Grade.class))).thenReturn(grade);

        Grade savedGrade = gradeService.saveGrade(new Grade());

        assertNotNull(savedGrade);
        assertEquals(grade.getDateReceived(), savedGrade.getDateReceived());

        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void testUpdateGrade_Found() {
        var student = new Student();
        var subject = new Subject();
        Grade updatedGrade = new Grade();
        updatedGrade.setStudent(student);
        updatedGrade.setSubject(subject);
        updatedGrade.setGrade(85);
        updatedGrade.setDateReceived(LocalDate.now());

        when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));
        when(gradeRepository.save(any(Grade.class))).thenReturn(updatedGrade);

        Grade result = gradeService.updateGrade(gradeId, updatedGrade);

        assertNotNull(result);
        assertEquals(subject, result.getSubject());
        assertEquals(85, result.getGrade());

        verify(gradeRepository, times(1)).findById(gradeId);
        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void testUpdateGrade_NotFound() {
        when(gradeRepository.findById(gradeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> gradeService.updateGrade(gradeId, grade));
        assertEquals("Grade not found for this id :: " + gradeId, exception.getMessage());

        verify(gradeRepository, times(1)).findById(gradeId);
        verify(gradeRepository, times(0)).save(any(Grade.class));
    }

    @Test
    void testDeleteGrade() {
        doNothing().when(gradeRepository).deleteById(gradeId);

        gradeService.deleteGrade(gradeId);

        verify(gradeRepository, times(1)).deleteById(gradeId);
    }

    @Test
    void testGetGradesByStudentId() {
        when(gradeRepository.findByStudentId(studentId)).thenReturn(List.of(grade));

        List<Grade> grades = gradeService.getGradesByStudentId(studentId);

        assertFalse(grades.isEmpty());
        assertEquals(1, grades.size());
        assertEquals(grade, grades.get(0));

        verify(gradeRepository, times(1)).findByStudentId(studentId);
    }
}
