package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingUpdateRequestDTO;
import com.example.pi_dev_ops_backend.domain.entities.ContractedListing;
import com.example.pi_dev_ops_backend.domain.entities.Listing;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import com.example.pi_dev_ops_backend.domain.mappers.ContractedListingMapper;
import com.example.pi_dev_ops_backend.domain.queryParams.ContractedListingPaginationParams;
import com.example.pi_dev_ops_backend.domain.specifications.ContractedListingSpecification;
import com.example.pi_dev_ops_backend.repository.ContractedListingRepository;
import com.example.pi_dev_ops_backend.services.exceptions.InvalidArgsException;
import com.example.pi_dev_ops_backend.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContractedListingService
{
    private final ContractedListingRepository contractedListingRepository;
    private final ListingService listingService;
    private final UserService userService;

    public Page<ContractedListingResponseDTO> findAll(ContractedListingPaginationParams paginationParams)
    {
        Pageable pageable = PageRequest.of(paginationParams.getPage(), paginationParams.getSize());
        Specification<ContractedListing> specification = ContractedListingSpecification.filter(paginationParams);
        return contractedListingRepository
                .findAll(specification, pageable)
                .map(ContractedListingMapper.INSTANCE::toContractedListingResponseDTO);
    }

    public ContractedListingResponseDTO findById(Long id)
    {
        ContractedListing contractedListing = findEntityById(id);
        return ContractedListingMapper.INSTANCE.toContractedListingResponseDTO(contractedListing);
    }

    public ContractedListingResponseDTO create(ContractedListingRequestDTO contractedListingRequestDTO, Authentication authentication)
    {
        User user = userService.findEntityByEmail(authentication.getName());
        if (user.getUserProfile() == null)
        {
            throw new InvalidArgsException("User does not have a profile");
        }

        Listing listing = listingService.findEntityById(contractedListingRequestDTO.listingId());
        UserProfile client = user.getUserProfile();

        ContractedListing contractedListing = ContractedListingMapper.INSTANCE.toContractedListing(contractedListingRequestDTO);
        contractedListing.setStatus("CONTRACTED");
        contractedListing.setListing(listing);
        contractedListing.setClient(client);

        contractedListing = contractedListingRepository.save(contractedListing);
        return ContractedListingMapper.INSTANCE.toContractedListingResponseDTO(contractedListing);
    }

    public ContractedListingResponseDTO update(Long id, ContractedListingRequestDTO contractedListingRequestDTO)
    {
        ContractedListing contractedListing = findEntityById(id);

        contractedListing.setStartedAt(contractedListingRequestDTO.startedAt() == null ? contractedListing.getStartedAt() : contractedListingRequestDTO.startedAt());
        contractedListing.setFinishedAt(contractedListingRequestDTO.finishedAt() == null ? contractedListing.getFinishedAt() : contractedListingRequestDTO.finishedAt());

        ContractedListing updatedContractedListing = contractedListingRepository.save(contractedListing);
        return ContractedListingMapper.INSTANCE.toContractedListingResponseDTO(updatedContractedListing);
    }

    public void delete(Long id)
    {
        ContractedListing contractedListing = findEntityById(id);
        contractedListingRepository.delete(contractedListing);
    }

    public ContractedListingResponseDTO updateStatus(
            Long id,
            ContractedListingUpdateRequestDTO contractedListingRequestDTO,
            Authentication authentication
    )
    {
        ContractedListing contractedListing = findEntityById(id);
        UserProfile userProfile = userService.findEntityByEmail(authentication.getName()).getUserProfile();

        String currentStatus = contractedListing.getStatus();
        String newStatus = contractedListingRequestDTO.status();
        String interactionOwner = Objects.equals(userProfile.getId(), contractedListing.getClient().getId()) ? "CLIENT" : "CONTRACTOR";

        if (!stateTransitionIsValid(currentStatus, newStatus, interactionOwner))
        {
            throw new InvalidArgsException("Invalid status transition");
        }

        ContractedListing updatedContractedListing = contractedListingRepository.save(contractedListing);
        return ContractedListingMapper.INSTANCE.toContractedListingResponseDTO(updatedContractedListing);
    }

    ContractedListing findEntityById(Long id)
    {
        return contractedListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ContractedListing.class, id));
    }

    private Boolean stateTransitionIsValid(String currentStatus, String newStatus, String interactionOwner)
    {
        if (Objects.equals(currentStatus, "ACCEPTED") || Objects.equals(currentStatus, "CANCELLED"))
            return false;

        if (newStatus.equals("CANCELLED"))
            return true;

        if (interactionOwner.equals("CLIENT"))
            return newStatus.equals("ACCEPTED") && Objects.equals(currentStatus, "FINISHED");
        else if (interactionOwner.equals("CONTRACTOR"))
        {
            if (newStatus.equals("STARTED") && Objects.equals(currentStatus, "CONTRACTED"))
                return true;
            return newStatus.equals("FINISHED") && Objects.equals(currentStatus, "STARTED");
        }
        return false;
    }

}
