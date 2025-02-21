package com.example.pi_dev_ops_backend.domain.dtos;

public record UserProfileRequestDTO(
        String name,
        String phone,
        String address,
        String postalCode
)
{
}
