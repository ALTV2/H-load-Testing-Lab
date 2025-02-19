package com.tveritin.repository;

import com.tveritin.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {
}
