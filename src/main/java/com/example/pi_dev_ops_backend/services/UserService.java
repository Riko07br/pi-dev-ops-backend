package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.UserRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.UserResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.mappers.UserMapper;
import com.example.pi_dev_ops_backend.domain.queryParams.PaginationParams;
import com.example.pi_dev_ops_backend.repository.UserRepository;
import com.example.pi_dev_ops_backend.services.exceptions.InvalidArgsException;
import com.example.pi_dev_ops_backend.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;

    public Page<UserResponseDTO> findAll(PaginationParams paginationParams)
    {
        Pageable pageable = PageRequest.of(paginationParams.getPage(), paginationParams.getSize());
        return userRepository.findAll(pageable).map(UserMapper.INSTANCE::toUserResponseDTO);
    }

    public UserResponseDTO findById(Long id)
    {
        User user = findEntityById(id);
        return UserMapper.INSTANCE.toUserResponseDTO(user);
    }

    public UserResponseDTO create(UserRequestDTO userRequestDTO)
    {
        if (userRepository.findByEmail(userRequestDTO.email()).isPresent())
            throw new InvalidArgsException("Email already in use");

        User user = new User(userRequestDTO.email(), userRequestDTO.password());

        return UserMapper.INSTANCE.toUserResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO)
    {
        User user = findEntityById(id);
        Optional<User> userOptional = userRepository.findByEmail(userRequestDTO.email());
        if (userOptional.isPresent() && !user.equals(userOptional.get()))
            throw new InvalidArgsException("Email already in use");

        user.setEmail(userRequestDTO.email());
        user.setPassword(userRequestDTO.password());

        return UserMapper.INSTANCE.toUserResponseDTO(userRepository.save(user));
    }

    public void delete(Long id)
    {
        userRepository.deleteById(id);
    }

    public UserResponseDTO findByEmail(String email)
    {
        User user = findEntityByEmail(email);
        return UserMapper.INSTANCE.toUserResponseDTO(user);
    }

    public User findEntityByEmail(String email)
    {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "email: " + email));
    }

    User findEntityById(Long id)
    {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, id));
    }

}
