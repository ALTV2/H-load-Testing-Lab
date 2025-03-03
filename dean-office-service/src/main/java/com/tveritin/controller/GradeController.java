package com.tveritin.controller;

import com.tveritin.entity.Grade;
import com.tveritin.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    @GetMapping
    public ResponseEntity<List<Grade>> getGradesByStudentId(@RequestParam UUID studentId) {
        return ResponseEntity.ok(gradeService.getGradesByStudentId(studentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable UUID id) {
        return ResponseEntity.ok(gradeService.getGradeById(id));
    }

    @PostMapping
    public ResponseEntity<Grade> createGrade(@RequestBody Grade grade) {
        return ResponseEntity.ok(gradeService.saveGrade(grade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable UUID id, @RequestBody Grade gradeDetails) {
        return ResponseEntity.ok(gradeService.updateGrade(id, gradeDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable UUID id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}