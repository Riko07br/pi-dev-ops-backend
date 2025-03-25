package com.example.pi_dev_ops_backend.domain.dtos;

public record ListingRequestDTO(
        String title,
        Float price,
        String description,
        String location
)
{
}
