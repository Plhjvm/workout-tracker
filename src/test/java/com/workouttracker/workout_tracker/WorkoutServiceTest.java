package com.workouttracker.workout_tracker;

import com.workouttracker.workout_tracker.entity.User;
import com.workouttracker.workout_tracker.entity.Workout;
import com.workouttracker.workout_tracker.exception.ResourceNotFoundException;
import com.workouttracker.workout_tracker.repository.ExerciseRepository;
import com.workouttracker.workout_tracker.repository.UserRepository;
import com.workouttracker.workout_tracker.repository.WorkoutRepository;
import com.workouttracker.workout_tracker.service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private WorkoutService workoutService;

    private Workout workout;

    @BeforeEach
    void setUp() {
        User owner = new User();
        owner.setId(1L);

        workout = Workout.builder()
                .id(10L)
                .user(owner)
                .startedAt(LocalDateTime.of(2026, 10, 12, 10, 0))
                .sets(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Should deny access when workout belongs to another user")
    void shouldDenyAccessForForeignWorkout() {
        when(workoutRepository.findById(10L)).thenReturn(Optional.of(workout));

        assertThatThrownBy(() -> workoutService.getById(10L, 2L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("You do not have access to this workout");
    }

    @Test
    @DisplayName("Should throw not found when workout does not exist")
    void shouldThrowWhenWorkoutMissing() {
        when(workoutRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutService.getById(99L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Workout not found with id: 99");
    }
}
