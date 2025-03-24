package com.example.pi_dev_ops_backend.controllers;

import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.queryParams.ContractedListingPaginationParams;
import com.example.pi_dev_ops_backend.services.ContractedListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping ("/contracted-listings")
@RequiredArgsConstructor
public class ContractedListingController
{
    private final ContractedListingService contractedListingService;

    @GetMapping
    public ResponseEntity<Page<ContractedListingResponseDTO>> findAll(
            ContractedListingPaginationParams paginationParams
    )
    {
        Page<ContractedListingResponseDTO> contractedListings = contractedListingService.findAll(paginationParams);
        return ResponseEntity.ok().body(contractedListings);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<ContractedListingResponseDTO> findById(@PathVariable Long id)
    {
        ContractedListingResponseDTO responseDTO = contractedListingService.findById(id);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping
    public ResponseEntity<ContractedListingResponseDTO> create(
            @RequestBody ContractedListingRequestDTO contractedListingRequestDTO,
            Authentication authentication
    ) throws URISyntaxException
    {
        ContractedListingResponseDTO responseDTO = contractedListingService.create(contractedListingRequestDTO, authentication);
        return ResponseEntity.created(new URI("/contracted-listings/" + responseDTO.id())).body(responseDTO);
    }

    //    @PutMapping ("/{id}")
    //    public ResponseEntity<ContractedListingResponseDTO> update(
    //            @PathVariable Long id,
    //            @RequestBody ContractedListingRequestDTO requestDTO
    //    )
    //    {
    //        ContractedListingResponseDTO responseDTO = contractedListingService.update(id, requestDTO);
    //        return ResponseEntity.ok().body(responseDTO);
    //    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        contractedListingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
