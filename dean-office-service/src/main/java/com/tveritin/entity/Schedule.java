package com.tveritin.entity;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "Schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
