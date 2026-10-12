package com.workouttracker.workout_tracker.service;

import com.workouttracker.workout_tracker.dto.ExerciseRequestDto;
import com.workouttracker.workout_tracker.dto.ExerciseResponseDto;
import com.workouttracker.workout_tracker.entity.ExerciseCatalog;
import com.workouttracker.workout_tracker.entity.MuscleGroup;
import com.workouttracker.workout_tracker.exception.ConflictException;
import com.workouttracker.workout_tracker.exception.ResourceNotFoundException;
import com.workouttracker.workout_tracker.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Transactional
    public ExerciseResponseDto createExercise(ExerciseRequestDto dto) {
        if (exerciseRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new ConflictException("Exercise with name '" + dto.getName() + "' already exists");
        }

        ExerciseCatalog exercise = ExerciseCatalog.builder()
                .name(dto.getName().trim())
                .muscleGroup(dto.getMuscleGroup())
                .description(dto.getDescription())
                .build();

        ExerciseCatalog saved = exerciseRepository.save(exercise);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ExerciseResponseDto> getExercises(MuscleGroup muscleGroup) {
        List<ExerciseCatalog> list = (muscleGroup != null)
                ? exerciseRepository.findByMuscleGroup(muscleGroup)
                : exerciseRepository.findAll();

        return list.stream()
                .map(this::mapToDto)
                .toList();
    }

    private ExerciseResponseDto mapToDto(ExerciseCatalog entity) {
        return ExerciseResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .muscleGroup(entity.getMuscleGroup())
                .description(entity.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public ExerciseResponseDto getById(Long id) {
        ExerciseCatalog exercise = exerciseRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Exercise with id '" + id + "' not found"));
        return mapToDto(exercise);
    }

    @Transactional
    public ExerciseResponseDto update(Long id, ExerciseRequestDto dto) {
        ExerciseCatalog exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise with id '" + id + "' not found"));

        String newName = dto.getName().trim();
        if (!exercise.getName().equalsIgnoreCase(newName)
                && exerciseRepository.existsByNameIgnoreCase(newName)) {
            throw new ConflictException("Exercise with name '" + newName + "' already exists");
        }

        exercise.setName(newName);
        exercise.setMuscleGroup(dto.getMuscleGroup());
        exercise.setDescription(dto.getDescription());
        ExerciseCatalog saved = exerciseRepository.save(exercise);
        return mapToDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        ExerciseCatalog exercise = exerciseRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Exercise with id '" + id + "' not found"));
        exerciseRepository.delete(exercise);
    }
}