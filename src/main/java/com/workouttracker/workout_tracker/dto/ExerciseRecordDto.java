package com.workouttracker.workout_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseRecordDto {
    private Long exerciseId;
    private String exerciseName;


    private BigDecimal maxWeight;
    private Integer repsAtMaxWeight;
    private LocalDateTime achievedAtMaxWeight;


    private BigDecimal estimated1RM;
    private BigDecimal weightAt1RM;
    private Integer repsAt1RM;
    private LocalDateTime achievedAt1RM;
}