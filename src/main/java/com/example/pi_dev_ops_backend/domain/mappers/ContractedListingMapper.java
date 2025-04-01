package com.example.pi_dev_ops_backend.domain.mappers;

import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.dtos.EvaluationResponseDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.dtos.UserProfileResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.ContractedListing;
import com.example.pi_dev_ops_backend.domain.queryParams.ContractedListingPaginationParams;
import com.example.pi_dev_ops_backend.domain.queryParams.ListingPaginationParams;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper (componentModel = "spring")
public interface ContractedListingMapper
{
    ContractedListingMapper INSTANCE = Mappers.getMapper(ContractedListingMapper.class);

    ContractedListing toContractedListing(ContractedListingRequestDTO contractedListingRequestDTO);

    default ContractedListingResponseDTO toContractedListingResponseDTO(
            ContractedListing contractedListing,
            ContractedListingPaginationParams paginationParams
    )
    {
        ListingResponseDTO listingResponseDTO = paginationParams.getIncludeListing()
                ? ListingMapper.INSTANCE.toListingResponseDTO(contractedListing.getListing(), new ListingPaginationParams(false))
                : null;
        UserProfileResponseDTO userProfileResponseDTO = paginationParams.getIncludeClient()
                ? UserProfileMapper.INSTANCE.toUserProfileResponseDTO(contractedListing.getClient())
                : null;
        EvaluationResponseDTO evaluationResponseDTO = paginationParams.getIncludeEvaluation() && contractedListing.getEvaluation() != null
                ? EvaluationMapper.INSTANCE.toEvaluationResponseDTO(contractedListing.getEvaluation())
                : null;
        return new ContractedListingResponseDTO(
                contractedListing.getId(),
                contractedListing.getStatus(),
                contractedListing.getClientRequest(),
                contractedListing.getStartedAt(),
                contractedListing.getFinishedAt(),
                listingResponseDTO,
                userProfileResponseDTO,
                evaluationResponseDTO
        );
    }
}
