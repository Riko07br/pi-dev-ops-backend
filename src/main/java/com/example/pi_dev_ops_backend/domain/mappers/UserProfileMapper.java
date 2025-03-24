package com.example.pi_dev_ops_backend.domain.mappers;

import com.example.pi_dev_ops_backend.domain.dtos.ListingUserProfileResponseDTO;
import com.example.pi_dev_ops_backend.domain.dtos.UserProfileRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.UserProfileResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.Skill;
import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper (componentModel = "spring")
public interface UserProfileMapper
{
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    @Mapping (target = "skills", ignore = true)
    UserProfile toUserProfile(UserProfileRequestDTO userProfileRequestDTO);

    ListingUserProfileResponseDTO toListingUserProfileResponseDTO(UserProfile userProfile);

    default UserProfileResponseDTO toUserProfileResponseDTO(UserProfile userProfile)
    {
        return new UserProfileResponseDTO(
                userProfile.getId(),
                userProfile.getName(),
                userProfile.getPhone(),
                userProfile.getAddress(),
                userProfile.getPostalCode(),
                userProfile.getSkills().stream().map(Skill::getName).collect(Collectors.toSet()),
                userProfile.getDocument(),
                userProfile.getDescription(),
                userProfile.getTitle()
        );
    }
}
