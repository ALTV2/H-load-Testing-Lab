package com.tveritin.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "Students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    
    private String email;
    private String phone;
}