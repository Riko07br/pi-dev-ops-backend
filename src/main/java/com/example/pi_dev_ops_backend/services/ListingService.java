package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.ListingRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.Listing;
import com.example.pi_dev_ops_backend.domain.entities.Skill;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.mappers.ListingMapper;
import com.example.pi_dev_ops_backend.domain.queryParams.ListingPaginationParams;
import com.example.pi_dev_ops_backend.domain.specifications.ListingSpecification;
import com.example.pi_dev_ops_backend.repository.ListingRepository;
import com.example.pi_dev_ops_backend.services.exceptions.InvalidArgsException;
import com.example.pi_dev_ops_backend.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ListingService
{
    private final ListingRepository listingRepository;
    private final UserService userService;
    private final SkillService skillService;

    public Page<ListingResponseDTO> findAll(ListingPaginationParams paginationParams)
    {
        Pageable pageable = paginationParams.getPageable();
        Specification<Listing> specification = ListingSpecification.filter(paginationParams);
        return listingRepository
                .findAll(specification, pageable)
                .map(listing ->ListingMapper.INSTANCE.toListingResponseDTO(listing, paginationParams));
    }

    public ListingResponseDTO findById(Long id)
    {
        Listing listing = findEntityById(id);
        return ListingMapper.INSTANCE.toListingResponseDTO(listing, new ListingPaginationParams());
    }

    public ListingResponseDTO create(ListingRequestDTO listingRequestDTO, Authentication authentication)
    {
        User user = userService.findEntityByEmail(authentication.getName());
        if (user.getUserProfile() == null)
        {
            throw new InvalidArgsException("User does not have a profile");
        }

        Listing listing = ListingMapper.INSTANCE.toListing(listingRequestDTO);
        listing.setCreationDate(LocalDate.now());
        listing.setUserProfile(user.getUserProfile());

        addSkillsToListing(listing, listingRequestDTO.skills());

        return ListingMapper.INSTANCE.toListingResponseDTO(listingRepository.save(listing), new ListingPaginationParams());
    }

    public ListingResponseDTO update(Long id, ListingRequestDTO listingRequestDTO)
    {
        Listing listing = findEntityById(id);

        listing.setTitle(listingRequestDTO.title());
        listing.setDescription(listingRequestDTO.description());
        listing.setPrice(listingRequestDTO.price());
        listing.setLocation(listingRequestDTO.location());

        addSkillsToListing(listing, listingRequestDTO.skills());

        return ListingMapper.INSTANCE.toListingResponseDTO(listingRepository.save(listing), new ListingPaginationParams());
    }

    public void delete(Long id)
    {
        Listing listing = findEntityById(id);
        listing.getSkills().clear();
        listingRepository.delete(listing);
    }

    Listing findEntityById(Long id)
    {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Listing.class, id));
    }

    private void addSkillsToListing(Listing listing, Collection<String> skillNames)
    {
        skillNames.forEach(skillName -> {
            try
            {
                Skill skill = skillService.findEntityByName(skillName);
                listing.addSkill(skill);
            }
            catch (ResourceNotFoundException e)
            {
                listing.addSkill(skillService.saveEntity(new Skill(skillName)));
            }
        });
    }

}
