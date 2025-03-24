package com.example.pi_dev_ops_backend.domain.dtos;

import java.time.Instant;

public record ContractedListingRequestDTO(
        String clientRequest,
        Instant startedAt,
        Instant finishedAt,
        Long listingId
)
{
}
