package com.workouttracker.workout_tracker.service;

import com.workouttracker.workout_tracker.dto.AuthUserResponseDto;
import com.workouttracker.workout_tracker.dto.LoginRequestDto;
import com.workouttracker.workout_tracker.dto.LoginResponseDto;
import com.workouttracker.workout_tracker.dto.RegisterRequestDto;
import com.workouttracker.workout_tracker.entity.User;
import com.workouttracker.workout_tracker.exception.ResourceNotFoundException;
import com.workouttracker.workout_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthUserResponseDto register(RegisterRequestDto dto) {
        

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResourceNotFoundException("Email already in use");
        }

        User user = User.builder()
            .email(dto.getEmail().trim())
            .username(dto.getUsername().trim())
            .password(passwordEncoder.encode(dto.getPassword().trim()))
            .build();
        User saved = userRepository.save(user);
        
        return AuthUserResponseDto.builder()
            .id(saved.getId())
            .email(saved.getEmail())
            .username(saved.getUsername())
            .build();
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user.getEmail());

        return LoginResponseDto.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
}