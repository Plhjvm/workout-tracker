package com.workouttracker.workout_tracker.dto;

import com.workouttracker.workout_tracker.entity.MuscleGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseRequestDto {

    @NotBlank(message = "Exercise name must not be blank")
    @Size(min = 2, max = 100, message = "Exercise name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Muscle group is required")
    private MuscleGroup muscleGroup;

    private String description;
}