package com.example.pi_dev_ops_backend.controllers;

import com.example.pi_dev_ops_backend.domain.dtos.UserProfileRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.UserProfileResponseDTO;
import com.example.pi_dev_ops_backend.domain.queryParams.PaginationParams;
import com.example.pi_dev_ops_backend.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping ("/user-profiles")
@RequiredArgsConstructor
public class UserProfileController
{
    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<Page<UserProfileResponseDTO>> findAll(PaginationParams paginationParams)
    {
        Page<UserProfileResponseDTO> userProfile = userProfileService.findAll(paginationParams);
        return ResponseEntity.ok().body(userProfile);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<UserProfileResponseDTO> findById(@PathVariable Long id)
    {
        UserProfileResponseDTO responseDTO = userProfileService.findById(id);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping
    public ResponseEntity<UserProfileResponseDTO> create(@RequestBody UserProfileRequestDTO userRequestDTO) throws URISyntaxException
    {
        UserProfileResponseDTO responseDTO = userProfileService.create(userRequestDTO.userId(), userRequestDTO);
        return ResponseEntity.created(new URI("/user-profiles/" + responseDTO.id())).body(responseDTO);
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<UserProfileResponseDTO> update(@PathVariable Long id, @RequestBody UserProfileRequestDTO requestDTO)
    {
        UserProfileResponseDTO responseDTO = userProfileService.update(id, requestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        userProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
