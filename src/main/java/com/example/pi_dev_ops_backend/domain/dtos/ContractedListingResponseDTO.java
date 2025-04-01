package com.example.pi_dev_ops_backend.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude (JsonInclude.Include.NON_NULL)
public record ContractedListingResponseDTO(
        Long id,
        String status,
        String clientRequest,
        @JsonInclude(JsonInclude.Include.ALWAYS)
        LocalDate startedAt,
        @JsonInclude(JsonInclude.Include.ALWAYS)
        LocalDate finishedAt,
        ListingResponseDTO listing,
        UserProfileResponseDTO client,
        EvaluationResponseDTO evaluation
)
{
}
