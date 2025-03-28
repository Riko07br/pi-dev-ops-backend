package com.example.pi_dev_ops_backend.domain.dtos;

import java.util.List;

public record ListingRequestDTO(
        String title,
        Float price,
        String description,
        String location,
        List<String> skills
)
{
}
