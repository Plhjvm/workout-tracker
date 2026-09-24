package com.workouttracker.workout_tracker.repository;

import com.workouttracker.workout_tracker.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {

    @Query("""
        SELECT ws FROM WorkoutSet ws
        JOIN ws.workout w
        WHERE w.user.id = :userId AND ws.exercise.id = :exerciseId
    """)
    List<WorkoutSet> findAllByUserIdAndExerciseId(@Param("userId") Long userId,
                                                  @Param("exerciseId") Long exerciseId);
}