package com.example.pi_dev_ops_backend.domain.dtos;

public record EvaluationRequestDTO(
        String comment,
        Integer stars,
        Long contractedListingId
)
{
}
