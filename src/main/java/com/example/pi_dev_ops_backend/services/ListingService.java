package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.ListingRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.Listing;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.mappers.ListingMapper;
import com.example.pi_dev_ops_backend.domain.queryParams.ListingPaginationParams;
import com.example.pi_dev_ops_backend.domain.queryParams.PaginationParams;
import com.example.pi_dev_ops_backend.domain.specifications.ListingSpecification;
import com.example.pi_dev_ops_backend.repository.ListingRepository;
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
public class ListingService
{
    private final ListingRepository listingRepository;
    private final UserService userService;

    public Page<ListingResponseDTO> findAll(ListingPaginationParams paginationParams)
    {
        Pageable pageable = PageRequest.of(paginationParams.getPage(), paginationParams.getSize());
        Specification<Listing> specification = ListingSpecification.filter(paginationParams);
        return listingRepository
                .findAll(specification, pageable)
                .map(ListingMapper.INSTANCE::toListingResponseDTO);
    }

    public ListingResponseDTO findById(Long id)
    {
        Listing listing = findEntityById(id);
        return ListingMapper.INSTANCE.toListingResponseDTO(listing);
    }

    public ListingResponseDTO create(ListingRequestDTO listingRequestDTO, Authentication authentication)
    {
        User user = userService.findEntityByEmail(authentication.getName());
        if (user.getUserProfile() == null)
        {
            throw new InvalidArgsException("User does not have a profile");
        }

        Listing listing = ListingMapper.INSTANCE.toListing(listingRequestDTO);
        listing.setUserProfile(user.getUserProfile());
        listing = listingRepository.save(listing);

        return ListingMapper.INSTANCE.toListingResponseDTO(listing);
    }

    public ListingResponseDTO update(Long id, ListingRequestDTO listingRequestDTO)
    {
        Listing listing = findEntityById(id);

        listing.setTitle(listingRequestDTO.title());
        listing.setDescription(listingRequestDTO.description());
        listing.setPrice(listingRequestDTO.price());

        return ListingMapper.INSTANCE.toListingResponseDTO(listingRepository.save(listing));
    }

    public void delete(Long id)
    {
        Listing listing = findEntityById(id);
        listingRepository.delete(listing);
    }

    Listing findEntityById(Long id)
    {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Listing.class, id));
    }

}
