package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.ListingRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.Listing;
import com.example.pi_dev_ops_backend.domain.mappers.ListingMapper;
import com.example.pi_dev_ops_backend.domain.queryParams.PaginationParams;
import com.example.pi_dev_ops_backend.repository.ListingRepository;
import com.example.pi_dev_ops_backend.services.exceptions.InvalidArgsException;
import com.example.pi_dev_ops_backend.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListingService
{
    private final ListingRepository listingRepository;

    public Page<ListingResponseDTO> findAll(PaginationParams paginationParams)
    {
        Pageable pageable = PageRequest.of(paginationParams.getPage(), paginationParams.getSize());
        return listingRepository.findAll(pageable).map(ListingMapper.INSTANCE::toListingResponseDTO);
    }

    public ListingResponseDTO findById(Long id)
    {
        Listing listing = findEntityById(id);
        return ListingMapper.INSTANCE.toListingResponseDTO(listing);
    }

//    public ListingResponseDTO create(ListingRequestDTO listingRequestDTO)
//    {
//        if (listingRepository.findByName(listingRequestDTO.name()).isPresent())
//            throw new InvalidArgsException("Listing already exists");
//
//        Listing listing = new Listing(listingRequestDTO.name());
//
//        return ListingMapper.INSTANCE.toListingResponseDTO(listingRepository.save(listing));
//    }

//    public ListingResponseDTO update(Long id, ListingRequestDTO listingRequestDTO)
//    {
//        Listing listing = findEntityById(id);
//        Optional<Listing> listingOptional = listingRepository.findByName(listingRequestDTO.name());
//        if (listingOptional.isPresent() && !listing.equals(listingOptional.get()))
//            throw new InvalidArgsException("Listing name already exists");
//
//        listing.setName(listingRequestDTO.name());
//
//        return ListingMapper.INSTANCE.toListingResponseDTO(listingRepository.save(listing));
//    }

    public void delete(Long id)
    {
        Listing listing = findEntityById(id);
        listingRepository.delete(listing);
    }

//    public Listing findEntityByName(String name)
//    {
//        return listingRepository.findByName(name)
//                .orElseThrow(() -> new ResourceNotFoundException(Listing.class, "name: " + name));
//    }

    Listing findEntityById(Long id)
    {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Listing.class, id));
    }

}
