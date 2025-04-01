package com.example.pi_dev_ops_backend.domain.dtos;

import java.time.LocalDate;

public record ContractedListingRequestDTO(
        String clientRequest,
        LocalDate startedAt,
        LocalDate finishedAt,
        Long listingId
)
{
}
