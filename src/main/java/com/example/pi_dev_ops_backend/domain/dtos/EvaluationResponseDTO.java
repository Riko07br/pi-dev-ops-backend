package com.example.pi_dev_ops_backend.domain.dtos;

public record EvaluationResponseDTO(
        Long id,
        String comment,
        Integer stars,
        Long contractedListingId
)
{
}
