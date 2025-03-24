package com.example.pi_dev_ops_backend.domain.dtos;

public record ListingUserProfileResponseDTO(
        Long id,
        String name,
        String description,
        String title
)
{
}
