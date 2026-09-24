package com.workouttracker.workout_tracker.controller;

import com.workouttracker.workout_tracker.dto.AuthUserResponseDto;
import com.workouttracker.workout_tracker.dto.LoginRequestDto;
import com.workouttracker.workout_tracker.dto.LoginResponseDto;
import com.workouttracker.workout_tracker.dto.RegisterRequestDto;
import com.workouttracker.workout_tracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthUserResponseDto> register(@Valid @RequestBody RegisterRequestDto dto) {
        AuthUserResponseDto response = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
