package com.workouttracker.workout_tracker.controller;

import com.workouttracker.workout_tracker.dto.ExerciseRequestDto;
import com.workouttracker.workout_tracker.dto.ExerciseResponseDto;
import com.workouttracker.workout_tracker.entity.MuscleGroup;
import com.workouttracker.workout_tracker.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.workouttracker.workout_tracker.dto.ExerciseRecordDto;
import com.workouttracker.workout_tracker.service.ExerciseRecordService;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    private final ExerciseRecordService exerciseRecordService;

    @GetMapping("/{id}/records")
    public ResponseEntity<ExerciseRecordDto> getExerciseRecord(
            @PathVariable Long id,
            @RequestParam Long userId) {
        return ResponseEntity.ok(exerciseRecordService.getExerciseRecord(userId, id));
    }

    @PostMapping
    public ResponseEntity<ExerciseResponseDto> createExercise(@Valid @RequestBody ExerciseRequestDto requestDto) {
        ExerciseResponseDto response = exerciseService.createExercise(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponseDto>> getAllExercises(
            @RequestParam(required = false) MuscleGroup muscleGroup
    ) {
        return ResponseEntity.ok(exerciseService.getExercises(muscleGroup));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDto>getExerciseById(@PathVariable Long id){
        return ResponseEntity.ok(exerciseService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponseDto> updateExercise(@PathVariable Long id, @Valid @RequestBody ExerciseRequestDto requestDto){
        return ResponseEntity.ok(exerciseService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id){
        exerciseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}