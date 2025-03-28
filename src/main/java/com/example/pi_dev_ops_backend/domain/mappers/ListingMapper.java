package com.example.pi_dev_ops_backend.domain.mappers;

import com.example.pi_dev_ops_backend.domain.dtos.ListingRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.Listing;
import com.example.pi_dev_ops_backend.domain.entities.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper (componentModel = "spring")
public interface ListingMapper
{
    ListingMapper INSTANCE = Mappers.getMapper(ListingMapper.class);

    @Mapping (target = "skills", ignore = true)
    Listing toListing(ListingRequestDTO listingRequestDTO);
    default ListingResponseDTO toListingResponseDTO(Listing listing)
    {
        return new ListingResponseDTO(
                listing.getId(),
                listing.getTitle(),
                listing.getPrice(),
                listing.getDescription(),
                listing.getLocation(),
                listing.getCreationDate(),
                UserProfileMapper.INSTANCE.toListingUserProfileResponseDTO(listing.getUserProfile()),
                listing.getSkills().stream().map(Skill::getName).toList()
        );
    }
}
