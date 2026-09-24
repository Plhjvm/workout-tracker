package com.workouttracker.workout_tracker.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutRequestDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Start time is required")
    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private String note;

    @NotEmpty(message = "Workout must contain at least one set")
    @Valid
    private List<WorkoutSetRequestDto> sets;
}