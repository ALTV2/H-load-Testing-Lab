package com.tveritin.repository;

import com.tveritin.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
}
