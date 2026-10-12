package com.workouttracker.workout_tracker;

import com.workouttracker.workout_tracker.entity.ExerciseCatalog;
import com.workouttracker.workout_tracker.entity.MuscleGroup;
import com.workouttracker.workout_tracker.entity.User;
import com.workouttracker.workout_tracker.repository.ExerciseRepository;
import com.workouttracker.workout_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedDemoUser();
        seedExercises();
    }

    private void seedDemoUser() {
        userRepository.findByEmail("test@example.com").ifPresentOrElse(user -> {
            String password = user.getPassword();
            if (password == null || !password.startsWith("$2")) {
                user.setPassword(passwordEncoder.encode("password"));
                userRepository.save(user);
            }
        }, () -> {
            User user = User.builder()
                    .email("test@example.com")
                    .username("demo_athlete")
                    .password(passwordEncoder.encode("password"))
                    .build();
            userRepository.save(user);
        });
    }

    private void seedExercises() {
        if (exerciseRepository.count() > 0) {
            return;
        }

        List<ExerciseCatalog> exercises = List.of(
                exercise("Bench Press", MuscleGroup.CHEST, "Barbell press on a flat bench"),
                exercise("Incline Dumbbell Press", MuscleGroup.CHEST, "Incline press with dumbbells"),
                exercise("Pull-up", MuscleGroup.BACK, "Bodyweight vertical pull"),
                exercise("Barbell Row", MuscleGroup.BACK, "Bent-over barbell row"),
                exercise("Back Squat", MuscleGroup.LEGS, "Barbell back squat"),
                exercise("Romanian Deadlift", MuscleGroup.LEGS, "Hip-hinge deadlift variation"),
                exercise("Overhead Press", MuscleGroup.SHOULDERS, "Standing barbell press"),
                exercise("Lateral Raise", MuscleGroup.SHOULDERS, "Dumbbell side raise"),
                exercise("Barbell Curl", MuscleGroup.BICEPS, "Standing barbell curl"),
                exercise("Tricep Pushdown", MuscleGroup.TRICEPS, "Cable pushdown"),
                exercise("Plank", MuscleGroup.CORE, "Isometric core hold"),
                exercise("Hanging Leg Raise", MuscleGroup.CORE, "Hanging core raise")
        );

        exerciseRepository.saveAll(exercises);
    }

    private ExerciseCatalog exercise(String name, MuscleGroup muscleGroup, String description) {
        return ExerciseCatalog.builder()
                .name(name)
                .muscleGroup(muscleGroup)
                .description(description)
                .build();
    }
}
