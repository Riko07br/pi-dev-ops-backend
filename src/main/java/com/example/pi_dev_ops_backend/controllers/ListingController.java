package com.example.pi_dev_ops_backend.controllers;

import com.example.pi_dev_ops_backend.domain.dtos.ListingRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.queryParams.ListingPaginationParams;
import com.example.pi_dev_ops_backend.domain.queryParams.PaginationParams;
import com.example.pi_dev_ops_backend.services.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping ("/listings")
@RequiredArgsConstructor
public class ListingController
{
    private final ListingService listingService;

    @GetMapping
    public ResponseEntity<Page<ListingResponseDTO>> findAll(ListingPaginationParams paginationParams)
    {
        Page<ListingResponseDTO> listings = listingService.findAll(paginationParams);
        return ResponseEntity.ok().body(listings);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<ListingResponseDTO> findById(@PathVariable Long id)
    {
        ListingResponseDTO responseDTO = listingService.findById(id);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping
    public ResponseEntity<ListingResponseDTO> create(
            @RequestBody ListingRequestDTO listingRequestDTO,
            Authentication authentication
    ) throws URISyntaxException
    {
        ListingResponseDTO responseDTO = listingService.create(listingRequestDTO, authentication);
        return ResponseEntity.created(new URI("/listings/" + responseDTO.id())).body(responseDTO);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<ListingResponseDTO> update(@PathVariable Long id, @RequestBody ListingRequestDTO requestDTO)
    {
        ListingResponseDTO responseDTO = listingService.update(id, requestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        listingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
