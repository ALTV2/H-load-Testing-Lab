package com.tveritin.service;

import com.tveritin.entity.Schedule;
import com.tveritin.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule getScheduleById(UUID id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(UUID id, Schedule scheduleDetails) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found for this id :: " + id));
        
        schedule.setGroup(scheduleDetails.getGroup());
        schedule.setSubject(scheduleDetails.getSubject());
        schedule.setTeacher(scheduleDetails.getTeacher());
        schedule.setDayOfWeek(scheduleDetails.getDayOfWeek());
        schedule.setStartTime(scheduleDetails.getStartTime());
        schedule.setEndTime(scheduleDetails.getEndTime());

        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(UUID id) {
        scheduleRepository.deleteById(id);
    }
}