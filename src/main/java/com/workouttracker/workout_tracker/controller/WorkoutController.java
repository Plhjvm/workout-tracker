package com.workouttracker.workout_tracker.controller;

import com.workouttracker.workout_tracker.dto.WorkoutRequestDto;
import com.workouttracker.workout_tracker.dto.WorkoutResponseDto;
import com.workouttracker.workout_tracker.dto.WorkoutSummaryResponseDto;
import com.workouttracker.workout_tracker.service.CurrentUserService;
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
    private final CurrentUserService currentUserService;

    @PostMapping
    public ResponseEntity<WorkoutResponseDto> createWorkout(@Valid @RequestBody WorkoutRequestDto request) {
        Long userId = currentUserService.requireCurrentUserId();
        WorkoutResponseDto response = workoutService.createWorkout(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WorkoutResponseDto>> getWorkouts() {
        Long userId = currentUserService.requireCurrentUserId();
        return ResponseEntity.ok(workoutService.getWorkoutsForUser(userId));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<WorkoutSummaryResponseDto> getWorkoutSummary(@PathVariable Long id) {
        Long userId = currentUserService.requireCurrentUserId();
        return ResponseEntity.ok(workoutService.getWorkoutSummary(id, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponseDto> getWorkoutById(@PathVariable Long id) {
        Long userId = currentUserService.requireCurrentUserId();
        return ResponseEntity.ok(workoutService.getById(id, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutResponseDto> updateWorkout(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutRequestDto requestDto
    ) {
        Long userId = currentUserService.requireCurrentUserId();
        return ResponseEntity.ok(workoutService.update(id, requestDto, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        Long userId = currentUserService.requireCurrentUserId();
        workoutService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}
