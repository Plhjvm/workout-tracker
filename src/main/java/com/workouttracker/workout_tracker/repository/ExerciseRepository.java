package com.workouttracker.workout_tracker.repository;

import com.workouttracker.workout_tracker.entity.ExerciseCatalog;
import com.workouttracker.workout_tracker.entity.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseCatalog, Long> {

    List<ExerciseCatalog> findByMuscleGroup(MuscleGroup muscleGroup);

    Optional<ExerciseCatalog> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}