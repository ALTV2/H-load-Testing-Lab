package com.tveritin.service;

import com.tveritin.entity.Subject;
import com.tveritin.entity.Teacher;
import com.tveritin.repository.SubjectRepository;
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
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    private Subject subject;
    private UUID subjectId;

    @BeforeEach
    void setUp() {
        subjectId = UUID.randomUUID();

        subject = new Subject();
        subject.setId(subjectId);
        subject.setName("Mathematics");
        subject.setTeacher(new Teacher());
    }

    @Test
    void testGetAllSubjects() {
        when(subjectRepository.findAll()).thenReturn(List.of(subject));

        List<Subject> subjects = subjectService.getAllSubjects();

        assertFalse(subjects.isEmpty());
        assertEquals(1, subjects.size());
        assertEquals(subject, subjects.get(0));

        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    void testGetSubjectById_Found() {
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));

        Subject foundSubject = subjectService.getSubjectById(subjectId);

        assertNotNull(foundSubject);
        assertEquals(subjectId, foundSubject.getId());

        verify(subjectRepository, times(1)).findById(subjectId);
    }

    @Test
    void testGetSubjectById_NotFound() {
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> subjectService.getSubjectById(subjectId));

        verify(subjectRepository, times(1)).findById(subjectId);
    }

    @Test
    void testSaveSubject() {
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);

        Subject savedSubject = subjectService.saveSubject(new Subject());

        assertNotNull(savedSubject);
        assertEquals(subject.getName(), savedSubject.getName());

        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void testUpdateSubject_Found() {
        Subject updatedSubject = new Subject();
        var teacher = new Teacher();

        updatedSubject.setName("Physics");
        updatedSubject.setTeacher(teacher);

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(any(Subject.class))).thenReturn(updatedSubject);

        Subject result = subjectService.updateSubject(subjectId, updatedSubject);

        assertNotNull(result);
        assertEquals("Physics", result.getName());
        assertEquals(teacher, result.getTeacher());

        verify(subjectRepository, times(1)).findById(subjectId);
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void testUpdateSubject_NotFound() {
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> subjectService.updateSubject(subjectId, subject));

        verify(subjectRepository, times(1)).findById(subjectId);
        verify(subjectRepository, times(0)).save(any(Subject.class));
    }

    @Test
    void testDeleteSubject() {
        doNothing().when(subjectRepository).deleteById(subjectId);

        subjectService.deleteSubject(subjectId);

        verify(subjectRepository, times(1)).deleteById(subjectId);
    }
}
