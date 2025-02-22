package com.example.pi_dev_ops_backend.security;

import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EmailAndPasswordProvider implements AuthenticationProvider
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public EmailAndPasswordProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(email, password, null);
        }
        throw new RuntimeException("Invalid password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
