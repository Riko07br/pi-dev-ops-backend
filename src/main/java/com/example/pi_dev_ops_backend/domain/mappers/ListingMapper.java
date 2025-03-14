package com.example.pi_dev_ops_backend.domain.mappers;

import com.example.pi_dev_ops_backend.domain.dtos.ListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.Listing;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper (componentModel = "spring")
public interface ListingMapper
{
    ListingMapper INSTANCE = Mappers.getMapper(ListingMapper.class);

    ListingResponseDTO toListingResponseDTO(Listing listing);
}
