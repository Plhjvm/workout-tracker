package com.workouttracker.workout_tracker;

import com.workouttracker.workout_tracker.entity.User;
import com.workouttracker.workout_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("test@example.com").isEmpty()) {
            User user = User.builder()
                    .email("test@example.com")
                    .username("demo_athlete")
                    .password("password")
                    .build();
            userRepository.save(user);
        }
    }
}