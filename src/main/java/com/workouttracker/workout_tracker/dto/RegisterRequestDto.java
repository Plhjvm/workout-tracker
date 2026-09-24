package com.workouttracker.workout_tracker.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterRequestDto {

    @NotBlank(message = "Email must not be blank")
    @Email
    private String email;

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6)
    private String password;
}
