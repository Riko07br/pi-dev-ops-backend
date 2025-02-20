package com.example.pi_dev_ops_backend.domain.mappers;

import com.example.pi_dev_ops_backend.domain.dtos.UserResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.User;

public class UserMapper
{
    public static UserResponseDTO toResponseDTO(User user)
    {
        return new UserResponseDTO(user.getId(), user.getEmail(), 1L);
    }
}
