package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.UserProfileRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.UserProfileResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.Skill;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import com.example.pi_dev_ops_backend.domain.queryParams.UserProfilePaginationParams;
import com.example.pi_dev_ops_backend.repository.UserProfileRepository;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
public class UserProfileServiceTest
{
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private UserService userService;
    @Mock
    private SkillService skillService;

    @InjectMocks
    private UserProfileService userProfileService;

    @Test
    public void findAll_ReturnsPagedUserProfiles() {
        UserProfilePaginationParams paginationParams = new UserProfilePaginationParams();
        Page<UserProfile> userProfiles = new PageImpl<>(List.of(new UserProfile()));

        when(userProfileRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(userProfiles);

        Page<UserProfileResponseDTO> result = userProfileService.findAll(paginationParams);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userProfileRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    public void findById_ReturnsUserProfile() {
        Long id = 1L;
        UserProfile userProfile = new UserProfile();
        userProfile.setId(id);

        when(userProfileRepository.findById(id)).thenReturn(Optional.of(userProfile));

        UserProfileResponseDTO result = userProfileService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(userProfileRepository, times(1)).findById(id);
    }

    @Test
    public void findById_NotFound_ThrowsResourceNotFoundException() {
        Long id = 1L;

        when(userProfileRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userProfileService.findById(id));
        verify(userProfileRepository, times(1)).findById(id);
    }

    @Test
    public void create_UserAlreadyHasProfile_ThrowsInvalidArgsException() {
        UserProfileRequestDTO userProfileRequestDTO = mockUserProfileRequestDTO();
        Authentication authentication = mock(Authentication.class);
        User user = new User();
        user.setUserProfile(new UserProfile());

        when(authentication.getName()).thenReturn("email@example.com");
        when(userService.findEntityByEmail("email@example.com")).thenReturn(user);

        assertThrows(InvalidArgsException.class, () -> userProfileService.create(userProfileRequestDTO, authentication));
        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    public void create_Success() {
        UserProfileRequestDTO userProfileRequestDTO = mockUserProfileRequestDTO();
        Authentication authentication = mock(Authentication.class);
        User user = new User();
        UserProfile userProfile = new UserProfile();
        user.setUserProfile(null);

        when(authentication.getName()).thenReturn("email@example.com");
        when(userService.findEntityByEmail("email@example.com")).thenReturn(user);
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);
        when(skillService.findEntityByName(any(String.class)))
                .thenAnswer(invocation -> new Skill(invocation.getArgument(0)));


        UserProfileResponseDTO result = userProfileService.create(userProfileRequestDTO, authentication);

        assertNotNull(result);
        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
    }

    @Test
    public void update_Success() {
        Long id = 1L;
        UserProfileRequestDTO userProfileRequestDTO = mockUserProfileRequestDTO();
        UserProfile userProfile = new UserProfile();
        userProfile.setId(id);

        when(userProfileRepository.findById(id)).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);
        when(skillService.findEntityByName(any(String.class)))
                .thenAnswer(invocation -> new Skill(invocation.getArgument(0)));

        UserProfileResponseDTO result = userProfileService.update(id, userProfileRequestDTO);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
    }

    @Test
    public void update_NotFound_ThrowsResourceNotFoundException() {
        Long id = 1L;
        UserProfileRequestDTO userProfileRequestDTO = mockUserProfileRequestDTO();

        when(userProfileRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userProfileService.update(id, userProfileRequestDTO));
        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    public void delete_Success() {
        Long id = 1L;
        UserProfile userProfile = new UserProfile();
        userProfile.setId(id);
        User user = new User();
        user.setUserProfile(userProfile);
        userProfile.setUser(user);

        when(userProfileRepository.findById(id)).thenReturn(Optional.of(userProfile));
        doNothing().when(userProfileRepository).delete(userProfile);

        userProfileService.delete(id);

        verify(userProfileRepository, times(1)).delete(userProfile);
        assertNull(user.getUserProfile());
    }

    @Test
    public void delete_NotFound_ThrowsResourceNotFoundException() {
        Long id = 1L;

        when(userProfileRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userProfileService.delete(id));
        verify(userProfileRepository, never()).delete(any(UserProfile.class));
    }

    private UserProfileRequestDTO mockUserProfileRequestDTO() {
        return new UserProfileRequestDTO(
                "User Name",
                "123456789",
                "User Address",
                "123456",
                Set.of("Skill 1", "Skill 2"),
                "Document",
                "Description",
                "Title"
        );
    }


}