package com.workouttracker.workout_tracker.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutSetRequestDto {

    @NotNull(message = "Exercise ID is required")
    private Long exerciseId;

    @NotNull(message = "Set order is required")
    @Positive
    private Integer setOrder;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal weight;

    @NotNull(message = "Reps count is required")
    @Positive
    private Integer reps;

    private Integer rpe;
}