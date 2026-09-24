package com.workouttracker.workout_tracker.service;

import com.workouttracker.workout_tracker.dto.ExerciseRecordDto;
import com.workouttracker.workout_tracker.entity.ExerciseCatalog;
import com.workouttracker.workout_tracker.entity.WorkoutSet;
import com.workouttracker.workout_tracker.exception.ResourceNotFoundException;
import com.workouttracker.workout_tracker.repository.ExerciseRepository;
import com.workouttracker.workout_tracker.repository.UserRepository;
import com.workouttracker.workout_tracker.repository.WorkoutSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseRecordService {

    private final WorkoutSetRepository workoutSetRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ExerciseRecordDto getExerciseRecord(Long userId, Long exerciseId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        ExerciseCatalog exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id: " + exerciseId));

        List<WorkoutSet> sets = workoutSetRepository.findAllByUserIdAndExerciseId(userId, exerciseId);

        if (sets.isEmpty()) {
            return ExerciseRecordDto.builder()
                    .exerciseId(exercise.getId())
                    .exerciseName(exercise.getName())
                    .build();
        }


        WorkoutSet maxWeightSet = sets.stream()
                .max(Comparator.comparing(WorkoutSet::getWeight))
                .orElseThrow();


        WorkoutSet best1RmSet = sets.stream()
                .max(Comparator.comparing(this::calculateEpley1RM))
                .orElseThrow();

        BigDecimal maxCalculated1RM = calculateEpley1RM(best1RmSet);

        return ExerciseRecordDto.builder()
                .exerciseId(exercise.getId())
                .exerciseName(exercise.getName())
                .maxWeight(maxWeightSet.getWeight())
                .repsAtMaxWeight(maxWeightSet.getReps())
                .achievedAtMaxWeight(maxWeightSet.getWorkout().getStartedAt())
                .estimated1RM(maxCalculated1RM)
                .weightAt1RM(best1RmSet.getWeight())
                .repsAt1RM(best1RmSet.getReps())
                .achievedAt1RM(best1RmSet.getWorkout().getStartedAt())
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