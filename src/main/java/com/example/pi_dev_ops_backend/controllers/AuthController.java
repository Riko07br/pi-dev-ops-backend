package com.example.pi_dev_ops_backend.controllers;

import com.example.pi_dev_ops_backend.domain.dtos.UserRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.UserResponseDTO;
import com.example.pi_dev_ops_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final UserService userService;

    @GetMapping ("/login")
    public ResponseEntity<UserResponseDTO> login(Authentication authentication)
    {
        UserResponseDTO responseDTO = userService.findByEmail(authentication.getName());
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping ("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO userRequestDTO)
    {
        UserResponseDTO responseDTO = userService.create(userRequestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping ("/refresh-token")
    public ResponseEntity<UserResponseDTO> refreshToken(Authentication authentication)
    {
        UserResponseDTO responseDTO = userService.findByEmail(authentication.getName());
        return ResponseEntity.ok().body(responseDTO);
    }
}
