package com.workouttracker.workout_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseProgressResponseDto {
    private Long exerciseId;
    private String exerciseName;
    private List<ExerciseProgressPointDto> points;
}
