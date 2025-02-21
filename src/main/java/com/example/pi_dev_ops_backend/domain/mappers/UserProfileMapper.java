package com.example.pi_dev_ops_backend.domain.mappers;

import com.example.pi_dev_ops_backend.domain.dtos.UserProfileRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.UserProfileResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserProfileMapper
{
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    UserProfile toUserProfile(UserProfileRequestDTO userProfileRequestDTO);

    UserProfileResponseDTO toUserProfileResponseDTO(UserProfile userProfile);
}
