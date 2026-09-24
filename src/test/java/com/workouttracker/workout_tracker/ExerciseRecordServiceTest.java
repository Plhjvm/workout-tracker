package com.workouttracker.workout_tracker;

import com.workouttracker.workout_tracker.dto.ExerciseRecordDto;
import com.workouttracker.workout_tracker.entity.ExerciseCatalog;
import com.workouttracker.workout_tracker.entity.User;
import com.workouttracker.workout_tracker.entity.Workout;
import com.workouttracker.workout_tracker.entity.WorkoutSet;
import com.workouttracker.workout_tracker.exception.ResourceNotFoundException;
import com.workouttracker.workout_tracker.repository.ExerciseRepository;
import com.workouttracker.workout_tracker.repository.UserRepository;
import com.workouttracker.workout_tracker.repository.WorkoutSetRepository;
import com.workouttracker.workout_tracker.service.ExerciseRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseRecordServiceTest {

    @Mock
    private WorkoutSetRepository workoutSetRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExerciseRecordService exerciseRecordService;

    private User testUser;
    private ExerciseCatalog testExercise;
    private Workout testWorkout;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testExercise = new ExerciseCatalog();
        testExercise.setId(1L);
        testExercise.setName("Bench Press");

        testWorkout = new Workout();
        testWorkout.setId(10L);
        testWorkout.setUser(testUser);
        testWorkout.setStartedAt(LocalDateTime.of(2026, 9, 16, 18, 0));
    }

    @Test
    @DisplayName("Должен корректно вычислить Max Weight и Estimated 1RM по формуле Эпли")
    void shouldCalculateCorrectMaxWeightAnd1RM() {
        // Подход 1: 100 кг на 1 повторение -> 1RM = 100
        WorkoutSet set1 = WorkoutSet.builder()
                .id(1L)
                .workout(testWorkout)
                .exercise(testExercise)
                .weight(new BigDecimal("100.00"))
                .reps(1)
                .build();

        // Подход 2: 85.5 кг на 8 повторений -> 1RM = 85.5 * (1 + 8/30) = 108.30
        WorkoutSet set2 = WorkoutSet.builder()
                .id(2L)
                .workout(testWorkout)
                .exercise(testExercise)
                .weight(new BigDecimal("85.50"))
                .reps(8)
                .build();

        when(userRepository.existsById(1L)).thenReturn(true);
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(testExercise));
        when(workoutSetRepository.findAllByUserIdAndExerciseId(1L, 1L)).thenReturn(List.of(set1, set2));

        ExerciseRecordDto result = exerciseRecordService.getExerciseRecord(1L, 1L);

        // Проверяем абсолютный максимум веса (должен победить сет 1 с весом 100)
        assertThat(result.getMaxWeight()).isEqualByComparingTo("100.00");
        assertThat(result.getRepsAtMaxWeight()).isEqualTo(1);

        // Проверяем расчетный 1RM (должен победить сет 2 с результатом ~108.30)
        assertThat(result.getEstimated1RM()).isEqualByComparingTo("108.30");
        assertThat(result.getWeightAt1RM()).isEqualByComparingTo("85.50");
        assertThat(result.getRepsAt1RM()).isEqualTo(8);

        verify(workoutSetRepository).findAllByUserIdAndExerciseId(1L, 1L);
    }

    @Test
    @DisplayName("Должен выбросить ResourceNotFoundException, если пользователь не найден")
    void shouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> exerciseRecordService.getExerciseRecord(999L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id: 999");
    }

    @Test
    @DisplayName("Должен возвращать пустые рекорды, если у пользователя нет подходов по упражнению")
    void shouldReturnEmptyRecordsWhenNoSetsFound() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(testExercise));
        when(workoutSetRepository.findAllByUserIdAndExerciseId(1L, 1L)).thenReturn(Collections.emptyList());

        ExerciseRecordDto result = exerciseRecordService.getExerciseRecord(1L, 1L);

        assertThat(result.getExerciseId()).isEqualTo(1L);
        assertThat(result.getExerciseName()).isEqualTo("Bench Press");
        assertThat(result.getMaxWeight()).isNull();
        assertThat(result.getEstimated1RM()).isNull();
    }
}