package com.tveritin.service;

import com.tveritin.entity.Teacher;
import com.tveritin.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;
    private UUID teacherId;

    @BeforeEach
    void setUp() {
        teacherId = UUID.randomUUID();

        teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setEmail("john.doe@example.com");
        teacher.setPhone("+123456789");
    }

    @Test
    void testGetAllTeachers() {
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));

        List<Teacher> teachers = teacherService.getAllTeachers();

        assertFalse(teachers.isEmpty());
        assertEquals(1, teachers.size());
        assertEquals(teacher, teachers.get(0));

        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void testGetTeacherById_Found() {
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        Teacher foundTeacher = teacherService.getTeacherById(teacherId);

        assertNotNull(foundTeacher);
        assertEquals(teacherId, foundTeacher.getId());

        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void testGetTeacherById_NotFound() {
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teacherService.getTeacherById(teacherId));

        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void testSaveTeacher() {
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        Teacher savedTeacher = teacherService.saveTeacher(new Teacher());

        assertNotNull(savedTeacher);
        assertEquals(teacher.getFirstName(), savedTeacher.getFirstName());

        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void testUpdateTeacher_Found() {
        Teacher updatedTeacher = new Teacher();
        updatedTeacher.setFirstName("Alice");
        updatedTeacher.setLastName("Smith");
        updatedTeacher.setEmail("alice.smith@example.com");
        updatedTeacher.setPhone("+987654321");

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(updatedTeacher);

        Teacher result = teacherService.updateTeacher(teacherId, updatedTeacher);

        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("alice.smith@example.com", result.getEmail());

        verify(teacherRepository, times(1)).findById(teacherId);
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void testUpdateTeacher_NotFound() {
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teacherService.updateTeacher(teacherId, teacher));

        verify(teacherRepository, times(1)).findById(teacherId);
        verify(teacherRepository, times(0)).save(any(Teacher.class));
    }

    @Test
    void testDeleteTeacher() {
        doNothing().when(teacherRepository).deleteById(teacherId);

        teacherService.deleteTeacher(teacherId);

        verify(teacherRepository, times(1)).deleteById(teacherId);
    }
}
