package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.ListingRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.Listing;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import com.example.pi_dev_ops_backend.domain.queryParams.ListingPaginationParams;
import com.example.pi_dev_ops_backend.repository.ListingRepository;
import com.example.pi_dev_ops_backend.services.exceptions.InvalidArgsException;
import com.example.pi_dev_ops_backend.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
public class ListingServiceTest
{
    @Mock
    private ListingRepository listingRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private ListingService listingService;


    @Test
    public void givenPaginationParams_whenFindAll_thenReturnsPagedListings() {
        ListingPaginationParams paginationParams = new ListingPaginationParams();
        Page<Listing> listings = new PageImpl<>(List.of(new Listing()));

        when(listingRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(listings);

        Page<ListingResponseDTO> result = listingService.findAll(paginationParams);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(listingRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    public void givenListingId_whenFindById_thenReturnsListing() {
        Long id = 1L;
        Listing listing = new Listing();
        listing.setId(id);

        when(listingRepository.findById(id)).thenReturn(Optional.of(listing));

        ListingResponseDTO result = listingService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(listingRepository, times(1)).findById(id);
    }

    @Test
    public void givenNonExistentId_whenFindById_thenThrowsResourceNotFoundException() {
        Long id = 1L;

        when(listingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> listingService.findById(id));
        verify(listingRepository, times(1)).findById(id);
    }

    @Test
    public void givenUserWithoutProfile_whenCreate_thenThrowsInvalidArgsException() {
        ListingRequestDTO listingRequestDTO = mock(ListingRequestDTO.class);
        Authentication authentication = mock(Authentication.class);
        User user = new User();
        user.setUserProfile(null);

        when(authentication.getName()).thenReturn("email@example.com");
        when(userService.findEntityByEmail("email@example.com")).thenReturn(user);

        assertThrows(InvalidArgsException.class, () -> listingService.create(listingRequestDTO, authentication));
        verify(listingRepository, never()).save(any(Listing.class));
    }

    @Test
    public void givenValidInput_whenCreate_thenSuccess() {
        ListingRequestDTO listingRequestDTO = mock(ListingRequestDTO.class);
        Authentication authentication = mock(Authentication.class);
        User user = new User();
        user.setUserProfile(new UserProfile());
        Listing listing = new Listing();

        when(authentication.getName()).thenReturn("email@example.com");
        when(userService.findEntityByEmail("email@example.com")).thenReturn(user);
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);

        ListingResponseDTO result = listingService.create(listingRequestDTO, authentication);

        assertNotNull(result);
        verify(listingRepository, times(1)).save(any(Listing.class));
    }

    @Test
    public void givenValidInput_whenUpdate_thenSuccess() {
        Long id = 1L;
        ListingRequestDTO listingRequestDTO = mock(ListingRequestDTO.class);
        Listing listing = new Listing();
        listing.setId(id);

        when(listingRepository.findById(id)).thenReturn(Optional.of(listing));
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);

        ListingResponseDTO result = listingService.update(id, listingRequestDTO);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(listingRepository, times(1)).save(any(Listing.class));
    }

    @Test
    public void givenNonExistentId_whenUpdate_thenThrowsResourceNotFoundException() {
        Long id = 1L;
        ListingRequestDTO listingRequestDTO = mock(ListingRequestDTO.class);

        when(listingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> listingService.update(id, listingRequestDTO));
        verify(listingRepository, never()).save(any(Listing.class));
    }

    @Test
    public void givenValidId_whenDelete_thenSuccess() {
        Long id = 1L;
        Listing listing = new Listing();
        listing.setId(id);

        when(listingRepository.findById(id)).thenReturn(Optional.of(listing));
        doNothing().when(listingRepository).delete(listing);

        listingService.delete(id);

        verify(listingRepository, times(1)).delete(listing);
    }

    @Test
    public void givenNonExistentId_whenDelete_thenThrowsResourceNotFoundException() {
        Long id = 1L;

        when(listingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> listingService.delete(id));
        verify(listingRepository, never()).delete(any(Listing.class));
    }
}
