package com.workouttracker.workout_tracker.dto;

import com.workouttracker.workout_tracker.entity.MuscleGroup;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExerciseResponseDto {
    private Long id;
    private String name;
    private MuscleGroup muscleGroup;
    private String description;
}