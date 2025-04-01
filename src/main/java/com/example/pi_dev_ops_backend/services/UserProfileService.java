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

import java.util.Collection;

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
        addSkillsToUserProfile(createdUserProfile, userProfileRequestDTO.skills());

        UserProfile userProfile = userProfileRepository.save(createdUserProfile);
        return UserProfileMapper.INSTANCE.toUserProfileResponseDTO(userProfile);
    }

    public UserProfileResponseDTO update(Long id, UserProfileRequestDTO userProfileRequestDTO)
    {
        UserProfile userProfile = findEntityById(id);
        userProfile.setName(userProfileRequestDTO.name() != null ? userProfileRequestDTO.name() : userProfile.getName());
        userProfile.setPhone(userProfileRequestDTO.phone() != null ? userProfileRequestDTO.phone() : userProfile.getPhone());
        userProfile.setAddress(userProfileRequestDTO.address() != null ? userProfileRequestDTO.address() : userProfile.getAddress());
        userProfile.setPostalCode(userProfileRequestDTO.postalCode() != null ? userProfileRequestDTO.postalCode() : userProfile.getPostalCode());
        userProfile.setDocument(userProfileRequestDTO.document() != null ? userProfileRequestDTO.document() : userProfile.getDocument());
        userProfile.setDescription(userProfileRequestDTO.description() != null ? userProfileRequestDTO.description() : userProfile.getDescription());
        userProfile.setTitle(userProfileRequestDTO.title() != null ? userProfileRequestDTO.title() : userProfile.getTitle());
        userProfile.getSkills().clear();
        addSkillsToUserProfile(userProfile, userProfileRequestDTO.skills());

        userProfile = userProfileRepository.save(userProfile);
        return UserProfileMapper.INSTANCE.toUserProfileResponseDTO(userProfile);
    }

    public void delete(Long id)
    {
        UserProfile userProfile = findEntityById(id);
        userProfile.getUser().setUserProfile(null);
        userProfile.getSkills().clear();
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

    private void addSkillsToUserProfile(UserProfile userProfile, Collection<String> skillNames)
    {
        skillNames.forEach(skillName -> {
            try
            {
                Skill skill = skillService.findEntityByName(skillName);
                userProfile.addSkill(skill);
            }
            catch (ResourceNotFoundException e)
            {
                userProfile.addSkill(skillService.saveEntity(new Skill(skillName)));
            }
        });
    }
}
