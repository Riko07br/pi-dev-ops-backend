package com.example.pi_dev_ops_backend.domain.dtos;

public record ListingResponseDTO(
        Long id,
        String title,
        Float price,
        String description,
        ListingUserProfileResponseDTO userProfile
)
{
}
