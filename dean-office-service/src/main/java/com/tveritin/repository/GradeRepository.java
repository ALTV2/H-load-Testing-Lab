package com.tveritin.repository;

import com.tveritin.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GradeRepository extends JpaRepository<Grade, UUID> {
}
