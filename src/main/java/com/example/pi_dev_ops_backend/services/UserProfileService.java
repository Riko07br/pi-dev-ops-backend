package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.UserProfileRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.UserProfileResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.Skill;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import com.example.pi_dev_ops_backend.domain.mappers.UserProfileMapper;
import com.example.pi_dev_ops_backend.domain.queryParams.UserProfilePaginationParams;
import com.example.pi_dev_ops_backend.domain.specifications.UserProfileSpecification;
import com.example.pi_dev_ops_backend.repository.UserProfileRepository;
import com.example.pi_dev_ops_backend.services.exceptions.InvalidArgsException;
import com.example.pi_dev_ops_backend.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService
{
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;
    private final SkillService skillService;

    public Page<UserProfileResponseDTO> findAll(UserProfilePaginationParams paginationParams)
    {
        Pageable pageable = paginationParams.getPageable();
        Specification<UserProfile> filter = UserProfileSpecification.filter(paginationParams);
        return userProfileRepository.findAll(filter,pageable).map(UserProfileMapper.INSTANCE::toUserProfileResponseDTO);
    }

    public UserProfileResponseDTO findById(Long id)
    {
        UserProfile user = findEntityById(id);
        return UserProfileMapper.INSTANCE.toUserProfileResponseDTO(user);
    }

    public UserProfileResponseDTO create(UserProfileRequestDTO userProfileRequestDTO, Authentication authentication)
    {
        User user = userService.findEntityByEmail(authentication.getName());
        if (user.getUserProfile() != null)
        {
            throw new InvalidArgsException("User already has a profile");
        }

        UserProfile createdUserProfile = UserProfileMapper.INSTANCE.toUserProfile(userProfileRequestDTO);
        createdUserProfile.setUser(user);
        user.setUserProfile(createdUserProfile);

        userProfileRequestDTO.skills().forEach(skillName -> {
            try
            {
                Skill skill = skillService.findEntityByName(skillName);
                createdUserProfile.addSkill(skill);
            }
            catch (ResourceNotFoundException e)
            {
                createdUserProfile.addSkill(new Skill(skillName));
            }
        });

        UserProfile userProfile = userProfileRepository.save(createdUserProfile);
        return UserProfileMapper.INSTANCE.toUserProfileResponseDTO(userProfile);
    }

    public UserProfileResponseDTO update(Long id, UserProfileRequestDTO userProfileRequestDTO)
    {
        findEntityById(id);
        UserProfile updatedUserProfile = UserProfileMapper.INSTANCE.toUserProfile(userProfileRequestDTO);
        updatedUserProfile.setId(id);

        userProfileRequestDTO.skills().forEach(skillName -> {
            try
            {
                Skill skill = skillService.findEntityByName(skillName);
                updatedUserProfile.addSkill(skill);
            }
            catch (ResourceNotFoundException e)
            {
                updatedUserProfile.addSkill(new Skill(skillName));
            }
        });

        UserProfile userProfile = userProfileRepository.save(updatedUserProfile);
        return UserProfileMapper.INSTANCE.toUserProfileResponseDTO(userProfile);
    }

    public void delete(Long id)
    {
        UserProfile userProfile = findEntityById(id);
        userProfile.getUser().setUserProfile(null);
        userProfileRepository.delete(userProfile);
    }

    UserProfile findEntityById(Long id)
    {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(UserProfile.class, id));
    }

    UserProfile findEntityByEmail(String email)
    {
        return userProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(UserProfile.class, email));
    }
}
