package com.tveritin.service;

import com.tveritin.entity.Subject;
import com.tveritin.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectById(UUID id) {
        return subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not found"));
    }

    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(UUID id, Subject subjectDetails) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not found for this id :: " + id));
        
        subject.setName(subjectDetails.getName());
        subject.setTeacher(subjectDetails.getTeacher());

        return subjectRepository.save(subject);
    }

    public void deleteSubject(UUID id) {
        subjectRepository.deleteById(id);
    }
}