package com.workouttracker.workout_tracker.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class AuthUserResponseDto {
    private Long id;
    private String email;
    private String username;
}
