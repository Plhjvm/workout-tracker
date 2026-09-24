package com.workouttracker.workout_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutSummaryResponseDto {
    private Long workoutId;
    private Long userId;
    private LocalDateTime startedAt;
    private BigDecimal totalTonnage;
    private int totalSets;
    private int totalReps;
    private Map<String, Integer> setsPerExercise;
}