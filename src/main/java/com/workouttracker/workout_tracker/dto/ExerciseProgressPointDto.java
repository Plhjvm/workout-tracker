package com.workouttracker.workout_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseProgressPointDto {
    private LocalDate date;
    private BigDecimal maxWeight;
    private BigDecimal estimated1RM;
}
