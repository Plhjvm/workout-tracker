package com.workouttracker.workout_tracker.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private String token;
    private Long id;
    private String email;
    private String username;
}
