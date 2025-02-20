package com.example.pi_dev_ops_backend.domain.mappers;

import com.example.pi_dev_ops_backend.domain.dtos.UserResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper
{
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping (source = "user.userProfile.id", target = "profileId")
    UserResponseDTO toUserResponseDTO(User user);
}
