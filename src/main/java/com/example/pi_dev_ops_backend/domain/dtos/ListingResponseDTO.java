package com.example.pi_dev_ops_backend.domain.dtos;

import java.time.LocalDate;
import java.util.List;

public record ListingResponseDTO(
        Long id,
        String title,
        Float price,
        String description,
        String location,
        LocalDate creationDate,
        ListingUserProfileResponseDTO userProfile,
        List<String> skills
)
{
}
