package com.example.pi_dev_ops_backend.domain.dtos;

import java.util.Set;

public record UserProfileRequestDTO(
        String name,
        String phone,
        String address,
        String postalCode,
        Set<String> skills,
        String document,
        String description,
        String title
)
{
}
