package com.tveritin.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "Subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}