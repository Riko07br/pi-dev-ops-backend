package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingResponseDTO;
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

        Listing listing =  listingService.findEntityById(contractedListingRequestDTO.listingId());
        UserProfile client = user.getUserProfile();

        ContractedListing contractedListing = ContractedListingMapper.INSTANCE.toContractedListing(contractedListingRequestDTO);
        contractedListing.setStatus("CONTRACTED");
        contractedListing.setListing(listing);
        contractedListing.setClient(client);

        contractedListing = contractedListingRepository.save(contractedListing);
        return ContractedListingMapper.INSTANCE.toContractedListingResponseDTO(contractedListing);
    }

//    public ContractedListingResponseDTO update(Long id, ContractedListingRequestDTO contractedListingRequestDTO)
//    {
//        ContractedListing contractedListing = findEntityById(id);
//
//        contractedListing.setStatus(contractedListingRequestDTO.status() == null ? contractedListing.getStatus() : contractedListingRequestDTO.status());
//        contractedListing.setStartedAt(contractedListingRequestDTO.startedAt() == null ? contractedListing.getStartedAt() : contractedListingRequestDTO.startedAt());
//        contractedListing.setFinishedAt(contractedListingRequestDTO.finishedAt() == null ? contractedListing.getFinishedAt() : contractedListingRequestDTO.finishedAt());
//
//        ContractedListing updatedContractedListing = contractedListingRepository.save(contractedListing);
//        return ContractedListingMapper.INSTANCE.toContractedListingResponseDTO(updatedContractedListing);
//    }

    public void delete(Long id)
    {
        ContractedListing contractedListing = findEntityById(id);
        contractedListingRepository.delete(contractedListing);
    }

    ContractedListing findEntityById(Long id)
    {
        return contractedListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ContractedListing.class, id));
    }

}
