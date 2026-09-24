package com.workouttracker.workout_tracker.service;

import com.workouttracker.workout_tracker.dto.WorkoutRequestDto;
import com.workouttracker.workout_tracker.dto.WorkoutResponseDto;
import com.workouttracker.workout_tracker.dto.WorkoutSummaryResponseDto;
import com.workouttracker.workout_tracker.entity.ExerciseCatalog;
import com.workouttracker.workout_tracker.entity.User;
import com.workouttracker.workout_tracker.entity.Workout;
import com.workouttracker.workout_tracker.entity.WorkoutSet;
import com.workouttracker.workout_tracker.exception.ResourceNotFoundException;
import com.workouttracker.workout_tracker.repository.ExerciseRepository;
import com.workouttracker.workout_tracker.repository.UserRepository;
import com.workouttracker.workout_tracker.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public WorkoutResponseDto createWorkout(WorkoutRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        Workout workout = Workout.builder()
                .user(user)
                .startedAt(dto.getStartedAt())
                .endedAt(dto.getEndedAt())
                .note(dto.getNote())
                .build();

        for (var setDto : dto.getSets()) {
            ExerciseCatalog exercise = exerciseRepository.findById(setDto.getExerciseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id: " + setDto.getExerciseId()));

            WorkoutSet set = WorkoutSet.builder()
                    .exercise(exercise)
                    .setOrder(setDto.getSetOrder())
                    .weight(setDto.getWeight())
                    .reps(setDto.getReps())
                    .rpe(setDto.getRpe())
                    .build();

            workout.addSet(set);
        }

        Workout savedWorkout = workoutRepository.save(workout);
        return mapToDto(savedWorkout);
    }

    @Transactional(readOnly = true)
    public List<WorkoutResponseDto> getWorkoutsByUserId(Long userId) {
        return workoutRepository.findByUserIdOrderByStartedAtDesc(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkoutSummaryResponseDto getWorkoutSummary(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + workoutId));


        BigDecimal totalTonnage = workout.getSets().stream()
                .map(set -> set.getWeight().multiply(BigDecimal.valueOf(set.getReps())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalReps = workout.getSets().stream()
                .mapToInt(WorkoutSet::getReps)
                .sum();

        int totalSets = workout.getSets().size();


        Map<String, Integer> setsPerExercise = workout.getSets().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        set -> set.getExercise().getName(),
                        java.util.stream.Collectors.collectingAndThen(
                                java.util.stream.Collectors.counting(),
                                Long::intValue
                        )
                ));

        return WorkoutSummaryResponseDto.builder()
                .workoutId(workout.getId())
                .userId(workout.getUser().getId())
                .startedAt(workout.getStartedAt())
                .totalTonnage(totalTonnage)
                .totalSets(totalSets)
                .totalReps(totalReps)
                .setsPerExercise(setsPerExercise)
                .build();
    }

    private WorkoutResponseDto mapToDto(Workout workout) {
        List<WorkoutResponseDto.WorkoutSetResponseDto> sets = workout.getSets().stream()
                .map(s -> WorkoutResponseDto.WorkoutSetResponseDto.builder()
                        .id(s.getId())
                        .exerciseId(s.getExercise().getId())
                        .exerciseName(s.getExercise().getName())
                        .setOrder(s.getSetOrder())
                        .weight(s.getWeight())
                        .reps(s.getReps())
                        .rpe(s.getRpe())
                        .build())
                .toList();

        return WorkoutResponseDto.builder()
                .id(workout.getId())
                .userId(workout.getUser().getId())
                .startedAt(workout.getStartedAt())
                .endedAt(workout.getEndedAt())
                .note(workout.getNote())
                .sets(sets)
                .build();
    }

    @Transactional(readOnly = true)
    public WorkoutResponseDto getById(Long id) {
        Workout workout = workoutRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Workout with id '" + id + "' not found"));
        return mapToDto(workout);
    }
    
    @Transactional
    public void delete(Long id) {
        Workout workout = workoutRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Workout with id '" + id + "' not found"));
        workoutRepository.delete(workout);
    }


    @Transactional
    public WorkoutResponseDto update(Long id, WorkoutRequestDto dto) {
        Workout workout = workoutRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Workout with id '" + id + "' not found"));
        workout.setStartedAt(dto.getStartedAt());
        workout.setEndedAt(dto.getEndedAt());
        workout.getSets().clear();
        workout.setNote(dto.getNote());
        for (var setDto : dto.getSets()) {
            ExerciseCatalog exercise = exerciseRepository.findById(setDto.getExerciseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id: " + setDto.getExerciseId()));

            WorkoutSet set = WorkoutSet.builder()
                    .exercise(exercise)
                    .setOrder(setDto.getSetOrder())
                    .weight(setDto.getWeight())
                    .reps(setDto.getReps())
                    .rpe(setDto.getRpe())
                    .build();

            workout.addSet(set);
        }

        Workout savedWorkout = workoutRepository.save(workout);
        return mapToDto(savedWorkout);
    }
}