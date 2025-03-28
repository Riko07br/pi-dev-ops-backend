package com.example.pi_dev_ops_backend.domain.mappers;

import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.ContractedListing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper (componentModel = "spring")
public interface ContractedListingMapper
{
    ContractedListingMapper INSTANCE = Mappers.getMapper(ContractedListingMapper.class);

    @Mapping (source = "listing.id", target = "listingId")
    @Mapping (source = "client.id", target = "clientId")
    @Mapping (source = "evaluation.id", target = "evaluationId")
    ContractedListingResponseDTO toContractedListingResponseDTO(ContractedListing contractedListing);
    ContractedListing toContractedListing(ContractedListingRequestDTO contractedListingRequestDTO);
}
