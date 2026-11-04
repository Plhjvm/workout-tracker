package com.workouttracker.workout_tracker.service;

import com.workouttracker.workout_tracker.dto.ExerciseProgressPointDto;
import com.workouttracker.workout_tracker.dto.ExerciseProgressResponseDto;
import com.workouttracker.workout_tracker.entity.ExerciseCatalog;
import com.workouttracker.workout_tracker.entity.WorkoutSet;
import com.workouttracker.workout_tracker.exception.ResourceNotFoundException;
import com.workouttracker.workout_tracker.repository.ExerciseRepository;
import com.workouttracker.workout_tracker.repository.WorkoutSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseProgressService {

    private final WorkoutSetRepository workoutSetRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional(readOnly = true)
    public ExerciseProgressResponseDto getProgress(Long userId, Long exerciseId) {
        ExerciseCatalog exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id: " + exerciseId));

        List<WorkoutSet> sets = workoutSetRepository.findAllByUserIdAndExerciseId(userId, exerciseId);

        if (sets.isEmpty()) {
            return ExerciseProgressResponseDto.builder()
                    .exerciseId(exercise.getId())
                    .exerciseName(exercise.getName())
                    .points(List.of())
                    .build();
        }

        Map<LocalDate, List<WorkoutSet>> byDate = sets.stream()
                .collect(Collectors.groupingBy(
                        set -> set.getWorkout().getStartedAt().toLocalDate()
                ));

        List<ExerciseProgressPointDto> points = byDate.entrySet().stream()
                .map(entry -> toPoint(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(ExerciseProgressPointDto::getDate))
                .toList();

        return ExerciseProgressResponseDto.builder()
                .exerciseId(exercise.getId())
                .exerciseName(exercise.getName())
                .points(points)
                .build();
    }

    private ExerciseProgressPointDto toPoint(LocalDate date, List<WorkoutSet> daySets) {
        BigDecimal maxWeight = daySets.stream()
                .map(WorkoutSet::getWeight)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        BigDecimal estimated1RM = daySets.stream()
                .map(this::calculateEpley1RM)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        return ExerciseProgressPointDto.builder()
                .date(date)
                .maxWeight(maxWeight)
                .estimated1RM(estimated1RM)
                .build();
    }

    private BigDecimal calculateEpley1RM(WorkoutSet set) {
        if (set.getReps() == 1) {
            return set.getWeight();
        }

        double multiplier = 1.0 + (set.getReps() / 30.0);
        return set.getWeight()
                .multiply(BigDecimal.valueOf(multiplier))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
