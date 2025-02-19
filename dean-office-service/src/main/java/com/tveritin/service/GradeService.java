package com.tveritin.service;

import com.tveritin.entity.Grade;
import com.tveritin.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Grade getGradeById(UUID id) {
        return gradeRepository.findById(id).orElseThrow(() -> new RuntimeException("Grade not found"));
    }

    public Grade saveGrade(Grade grade) {
        grade.setDateReceived(LocalDate.now()); // Устанавливаем текущую дату при сохранении
        return gradeRepository.save(grade);
    }

    public Grade updateGrade(UUID id, Grade gradeDetails) {
        Grade grade = gradeRepository.findById(id).orElseThrow(() -> new RuntimeException("Grade not found for this id :: " + id));
        
        grade.setStudent(gradeDetails.getStudent());
        grade.setSubject(gradeDetails.getSubject());
        grade.setGrade(gradeDetails.getGrade());
        grade.setDateReceived(gradeDetails.getDateReceived());

        return gradeRepository.save(grade);
    }

    public void deleteGrade(UUID id) {
        gradeRepository.deleteById(id);
    }
}