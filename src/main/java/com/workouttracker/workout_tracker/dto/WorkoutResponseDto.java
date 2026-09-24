package com.workouttracker.workout_tracker.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutResponseDto {

    private Long id;
    private Long userId;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String note;
    private List<WorkoutSetResponseDto> sets;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WorkoutSetResponseDto {
        private Long id;
        private Long exerciseId;
        private String exerciseName;
        private Integer setOrder;
        private BigDecimal weight;
        private Integer reps;
        private Integer rpe;
    }
}