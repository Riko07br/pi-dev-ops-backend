package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingResponseDTO;
import com.example.pi_dev_ops_backend.domain.dtos.ContractedListingUpdateRequestDTO;
import com.example.pi_dev_ops_backend.domain.entities.ContractedListing;
import com.example.pi_dev_ops_backend.domain.entities.Listing;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import com.example.pi_dev_ops_backend.domain.queryParams.ContractedListingPaginationParams;
import com.example.pi_dev_ops_backend.repository.ContractedListingRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
public class ContractedListingServiceTest
{
    @Mock
    private ContractedListingRepository contractedListingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ListingService listingService;

    @InjectMocks
    private ContractedListingService contractedListingService;

    @Test
    public void givenValidPaginationParams_whenFindAll_thenReturnsPagedContractedListings()
    {
        ContractedListingPaginationParams paginationParams = new ContractedListingPaginationParams();
        Page<ContractedListing> contractedListings = new PageImpl<>(List.of(mockContractedListing(1L)));

        when(contractedListingRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(contractedListings);

        Page<ContractedListingResponseDTO> result = contractedListingService.findAll(paginationParams);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(contractedListingRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    public void givenValidId_whenFindById_thenReturnsContractedListing()
    {
        Long id = 1L;
        ContractedListing contractedListing = mockContractedListing(id);

        when(contractedListingRepository.findById(id)).thenReturn(Optional.of(contractedListing));

        ContractedListingResponseDTO result = contractedListingService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(contractedListingRepository, times(1)).findById(id);
    }

    @Test
    public void givenInvalidId_whenFindById_thenThrowsResourceNotFoundException()
    {
        Long id = 1L;

        when(contractedListingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contractedListingService.findById(id));
        verify(contractedListingRepository, times(1)).findById(id);
    }

    @Test
    public void givenUserWithoutProfile_whenCreate_thenThrowsInvalidArgsException()
    {
        ContractedListingRequestDTO requestDTO = mock(ContractedListingRequestDTO.class);
        Authentication authentication = mock(Authentication.class);
        User user = new User();
        user.setUserProfile(null);

        when(authentication.getName()).thenReturn("email@example.com");
        when(userService.findEntityByEmail("email@example.com")).thenReturn(user);

        assertThrows(InvalidArgsException.class, () -> contractedListingService.create(requestDTO, authentication));
        verify(contractedListingRepository, never()).save(any(ContractedListing.class));
    }

    @Test
    public void givenValidRequest_whenCreate_thenReturnsCreatedContractedListing()
    {
        ContractedListingRequestDTO requestDTO = mockContractedListingRequestDTO(1L);
        Authentication authentication = mock(Authentication.class);
        ContractedListing contractedListing = mockContractedListing(1L);
        User user = new User();
        user.setUserProfile(contractedListing.getClient());

        when(authentication.getName()).thenReturn("email@example.com");
        when(userService.findEntityByEmail("email@example.com")).thenReturn(user);
        when(listingService.findEntityById(any(Long.class))).thenReturn(contractedListing.getListing());
        when(contractedListingRepository.save(any(ContractedListing.class))).thenReturn(contractedListing);

        ContractedListingResponseDTO result = contractedListingService.create(requestDTO, authentication);

        assertNotNull(result);
        verify(contractedListingRepository, times(1)).save(any(ContractedListing.class));
    }

    @Test
    public void givenValidIdAndRequest_whenUpdate_thenReturnsUpdatedContractedListing()
    {
        Long id = 1L;
        ContractedListingRequestDTO requestDTO = mockContractedListingRequestDTO(id);
        ContractedListing contractedListing = mockContractedListing(id);

        when(contractedListingRepository.findById(id)).thenReturn(Optional.of(contractedListing));
        when(contractedListingRepository.save(any(ContractedListing.class))).thenReturn(contractedListing);

        ContractedListingResponseDTO result = contractedListingService.update(id, requestDTO);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(contractedListingRepository, times(1)).save(any(ContractedListing.class));
    }

    @Test
    public void givenInvalidId_whenUpdate_thenThrowsResourceNotFoundException()
    {
        Long id = 1L;
        ContractedListingRequestDTO requestDTO = mock(ContractedListingRequestDTO.class);

        when(contractedListingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contractedListingService.update(id, requestDTO));
        verify(contractedListingRepository, never()).save(any(ContractedListing.class));
    }

    @Test
    public void givenValidId_whenDelete_thenDeletesContractedListing()
    {
        Long id = 1L;
        ContractedListing contractedListing = new ContractedListing();
        contractedListing.setId(id);

        when(contractedListingRepository.findById(id)).thenReturn(Optional.of(contractedListing));
        doNothing().when(contractedListingRepository).delete(contractedListing);

        contractedListingService.delete(id);

        verify(contractedListingRepository, times(1)).delete(contractedListing);
    }

    @Test
    public void givenInvalidId_whenDelete_thenThrowsResourceNotFoundException()
    {
        Long id = 1L;

        when(contractedListingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contractedListingService.delete(id));
        verify(contractedListingRepository, never()).delete(any(ContractedListing.class));
    }

    @Test
    public void givenInvalidStatusTransition_whenUpdateStatus_thenThrowsInvalidArgsException()
    {
        Long id = 1L;
        ContractedListingUpdateRequestDTO requestDTO = mock(ContractedListingUpdateRequestDTO.class);
        Authentication authentication = mock(Authentication.class);
        ContractedListing contractedListing = new ContractedListing();
        User user = new User();
        UserProfile userProfile = new UserProfile();
        user.setUserProfile(userProfile);

        contractedListing.setStatus("ACCEPTED");
        contractedListing.setClient(userProfile);

        when(contractedListingRepository.findById(id)).thenReturn(Optional.of(contractedListing));
        when(authentication.getName()).thenReturn("email@example.com");
        when(userService.findEntityByEmail("email@example.com")).thenReturn(user);
        when(requestDTO.status()).thenReturn("STARTED");

        assertThrows(InvalidArgsException.class, () -> contractedListingService.updateStatus(id, requestDTO, authentication));
        verify(contractedListingRepository, never()).save(any(ContractedListing.class));
    }

    private ContractedListing mockContractedListing(Long id)
    {
        ContractedListing contractedListing = new ContractedListing();
        contractedListing.setId(id);
        contractedListing.setListing(new Listing());
        contractedListing.setClient(new UserProfile());
        return contractedListing;
    }

    private ContractedListingRequestDTO mockContractedListingRequestDTO(Long id)
    {
        return new ContractedListingRequestDTO(
                "Client request",
                LocalDate.now(),
                LocalDate.now(),
                id

        );
    }
}
