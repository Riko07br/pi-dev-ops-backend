package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.UserProfileRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.UserProfileResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import com.example.pi_dev_ops_backend.domain.mappers.UserProfileMapper;
import com.example.pi_dev_ops_backend.domain.queryParams.PaginationParams;
import com.example.pi_dev_ops_backend.repository.UserProfileRepository;
import com.example.pi_dev_ops_backend.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService
{
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;

    public Page<UserProfileResponseDTO> findAll(PaginationParams paginationParams)
    {
        Pageable pageable = PageRequest.of(paginationParams.getPage(), paginationParams.getSize());
        return userProfileRepository.findAll(pageable).map(UserProfileMapper.INSTANCE::toUserProfileResponseDTO);
    }

    public UserProfileResponseDTO findById(Long id)
    {
        UserProfile user = findEntityById(id);
        return UserProfileMapper.INSTANCE.toUserProfileResponseDTO(user);
    }

    public UserProfileResponseDTO create(Long userId, UserProfileRequestDTO userProfileRequestDTO)
    {
        User user = userService.findEntityById(userId);
        UserProfile userProfile = UserProfileMapper.INSTANCE.toUserProfile(userProfileRequestDTO);
        userProfile.setUser(user);
        userProfile = userProfileRepository.save(userProfile);
        return UserProfileMapper.INSTANCE.toUserProfileResponseDTO(userProfile);
    }

    public UserProfileResponseDTO update(Long id, UserProfileRequestDTO userRequestDTO)
    {
        UserProfile userProfile = findEntityById(id);
        userProfile.setName(userRequestDTO.name() != null ? userRequestDTO.name() : userProfile.getName());
        userProfile.setPhone(userRequestDTO.phone() != null ? userRequestDTO.phone() : userProfile.getPhone());
        userProfile.setAddress(userRequestDTO.address() != null ? userRequestDTO.address() : userProfile.getAddress());
        userProfile.setPostalCode(userRequestDTO.postalCode() != null ? userRequestDTO.postalCode() : userProfile.getPostalCode());
        userProfile = userProfileRepository.save(userProfile);
        return UserProfileMapper.INSTANCE.toUserProfileResponseDTO(userProfile);
    }

    public void delete(Long id)
    {
        userProfileRepository.deleteById(id);
    }

    UserProfile findEntityById(Long id)
    {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(UserProfile.class, id));
    }
}
