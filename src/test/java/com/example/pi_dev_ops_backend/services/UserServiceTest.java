package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.UserRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.UserResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.repository.UserRepository;
import com.example.pi_dev_ops_backend.services.exceptions.InvalidArgsException;
import com.example.pi_dev_ops_backend.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void createUser_EmailAlreadyInUse_ThrowsInvalidArgsException() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("email@example.com", "password");

        when(userRepository.findByEmail(userRequestDTO.email())).thenReturn(Optional.of(new User()));

        assertThrows(InvalidArgsException.class, () -> userService.create(userRequestDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void updateUser_Success() {
        Long userId = 1L;
        UserRequestDTO userRequestDTO = new UserRequestDTO("newemail@example.com", "newpassword");
        User user = new User("oldemail@example.com", "oldpassword");
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userRequestDTO.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(userRequestDTO.password())).thenReturn("encodedpassword");

        UserResponseDTO updatedUser = userService.update(userId, userRequestDTO);

        assertNotNull(updatedUser);
        assertEquals(userRequestDTO.email(), updatedUser.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateUser_EmailAlreadyInUse_ThrowsInvalidArgsException() {
        Long userId = 1L;
        UserRequestDTO userRequestDTO = new UserRequestDTO("email@example.com", "newpassword");
        User user = new User("oldemail@example.com", "oldpassword");
        user.setId(userId);
        User anotherUser = new User("email@example.com", "password");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userRequestDTO.email())).thenReturn(Optional.of(anotherUser));

        assertThrows(InvalidArgsException.class, () -> userService.update(userId, userRequestDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void deleteUser_Success() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void findByEmail_Success() {
        String email = "email@example.com";
        User user = new User(email, "password");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserResponseDTO foundUser = userService.findByEmail(email);

        assertNotNull(foundUser);
        assertEquals(email, foundUser.email());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void findByEmail_NotFound_ThrowsResourceNotFoundException() {
        String email = "email@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findByEmail(email));
        verify(userRepository, times(1)).findByEmail(email);
    }


}