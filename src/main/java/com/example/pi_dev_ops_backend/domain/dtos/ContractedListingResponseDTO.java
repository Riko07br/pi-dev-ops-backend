package com.example.pi_dev_ops_backend.domain.dtos;

import java.time.Instant;

public record ContractedListingResponseDTO(
        Long id,
        String status,
        String clientRequest,
        Instant startedAt,
        Instant finishedAt,
        Long listingId,
        Long clientId,
        Integer evaluationId
)
{
}
