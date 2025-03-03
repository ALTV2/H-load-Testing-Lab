package com.tveritin.service;

import com.tveritin.entity.*;
import com.tveritin.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    private Schedule schedule;
    private UUID scheduleId;
    private UUID groupId;

    @BeforeEach
    void setUp() {
        scheduleId = UUID.randomUUID();
        groupId = UUID.randomUUID();

        var teacher = new Teacher();
        teacher.setFirstName("Evgeniy");
        teacher.setFirstName("Vatlin");

        var subject = new Subject();
        subject.setName("Math");
        subject.setTeacher(teacher);

        schedule = new Schedule();
        schedule.setId(scheduleId);
        schedule.setGroup(new Group());
        schedule.setSubject(subject);
        schedule.setTeacher(teacher);
        schedule.setDayOfWeek("Monday");
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(10, 30));
    }

    @Test
    void testGetAllSchedules() {
        when(scheduleRepository.findAll()).thenReturn(List.of(schedule));

        List<Schedule> schedules = scheduleService.getAllSchedules();

        assertFalse(schedules.isEmpty());
        assertEquals(1, schedules.size());
        assertEquals(schedule, schedules.get(0));

        verify(scheduleRepository, times(1)).findAll();
    }

    @Test
    void testGetScheduleById_Found() {
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        Schedule foundSchedule = scheduleService.getScheduleById(scheduleId);

        assertNotNull(foundSchedule);
        assertEquals(scheduleId, foundSchedule.getId());

        verify(scheduleRepository, times(1)).findById(scheduleId);
    }

    @Test
    void testGetScheduleById_NotFound() {
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> scheduleService.getScheduleById(scheduleId));
        assertEquals("Schedule not found", exception.getMessage());

        verify(scheduleRepository, times(1)).findById(scheduleId);
    }

    @Test
    void testSaveSchedule() {
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule savedSchedule = scheduleService.saveSchedule(new Schedule());

        assertNotNull(savedSchedule);
        assertEquals(schedule.getSubject(), savedSchedule.getSubject());

        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    void testUpdateSchedule_Found() {
        Schedule updatedSchedule = new Schedule();
        var subject = new Subject();
        var teacher = new Teacher();
        var group = new Group();

        updatedSchedule.setGroup(group);
        updatedSchedule.setSubject(subject);
        updatedSchedule.setTeacher(teacher);
        updatedSchedule.setDayOfWeek("Wednesday");
        updatedSchedule.setStartTime(LocalTime.of(10, 0));
        updatedSchedule.setEndTime(LocalTime.of(11, 30));

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(updatedSchedule);

        Schedule result = scheduleService.updateSchedule(scheduleId, updatedSchedule);

        assertNotNull(result);
        assertEquals(subject, result.getSubject());
        assertEquals(teacher, result.getTeacher());
        assertEquals("Wednesday", result.getDayOfWeek());

        verify(scheduleRepository, times(1)).findById(scheduleId);
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    void testUpdateSchedule_NotFound() {
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> scheduleService.updateSchedule(scheduleId, schedule));
        assertEquals("Schedule not found for this id :: " + scheduleId, exception.getMessage());

        verify(scheduleRepository, times(1)).findById(scheduleId);
        verify(scheduleRepository, times(0)).save(any(Schedule.class));
    }

    @Test
    void testDeleteSchedule() {
        doNothing().when(scheduleRepository).deleteById(scheduleId);

        scheduleService.deleteSchedule(scheduleId);

        verify(scheduleRepository, times(1)).deleteById(scheduleId);
    }

    @Test
    void testGetAllSchedulesByGroupId() {
        when(scheduleRepository.findByGroupId(groupId)).thenReturn(List.of(schedule));

        List<Schedule> schedules = scheduleService.getAllSchedulesByGroupId(groupId);

        assertFalse(schedules.isEmpty());
        assertEquals(1, schedules.size());
        assertEquals(schedule, schedules.get(0));

        verify(scheduleRepository, times(1)).findByGroupId(groupId);
    }
}
