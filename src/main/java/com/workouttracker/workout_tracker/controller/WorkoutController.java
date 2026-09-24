package com.workouttracker.workout_tracker.controller;

import com.workouttracker.workout_tracker.dto.WorkoutSummaryResponseDto;
import com.workouttracker.workout_tracker.dto.WorkoutRequestDto;
import com.workouttracker.workout_tracker.dto.WorkoutResponseDto;
import com.workouttracker.workout_tracker.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<WorkoutResponseDto> createWorkout(@Valid @RequestBody WorkoutRequestDto request) {
        WorkoutResponseDto response = workoutService.createWorkout(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WorkoutResponseDto>> getWorkouts(@RequestParam Long userId) {
        return ResponseEntity.ok(workoutService.getWorkoutsByUserId(userId));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<WorkoutSummaryResponseDto> getWorkoutSummary(@PathVariable Long id) {
        return ResponseEntity.ok(workoutService.getWorkoutSummary(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponseDto> getWorkoutById(@PathVariable Long id){
        return ResponseEntity.ok(workoutService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutResponseDto> updateWorkout(@PathVariable Long id, @Valid @RequestBody WorkoutRequestDto requestDto){
        return ResponseEntity.ok(workoutService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id){
        workoutService.delete(id);
        return ResponseEntity.noContent().build();
    }
}