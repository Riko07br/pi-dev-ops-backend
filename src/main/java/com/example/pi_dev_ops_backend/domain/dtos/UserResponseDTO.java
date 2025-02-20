package com.example.pi_dev_ops_backend.domain.dtos;

public record UserResponseDTO(
        Long id,
        String email,
        Long profileId
)
{
}
