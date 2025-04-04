package com.example.pi_dev_ops_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.repository.UserRepository;
import com.example.pi_dev_ops_backend.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class JWTRefreshFilter extends OncePerRequestFilter
{
    private final String jwtSecretKey;
    private final UserRepository userRepository;

    public JWTRefreshFilter(String jwtSecretKey, UserRepository userRepository)
    {
        this.userRepository = userRepository;
        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
        {
            try
            {
                Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
                String email = authentication.getName();

                User user = userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException(User.class, "email: " + email));

                String newToken = JWT.create()
                        .withIssuer("SeriousBizness")
                        .withSubject("User Info")
                        .withClaim("username", user.getEmail())
                        .withClaim("userId", user.getId())
                        .withClaim("accountId", user.getUserProfile() == null ? null : user.getUserProfile().getId())
                        .withIssuedAt(Instant.now())
                        .withExpiresAt(Instant.now().plus(5, ChronoUnit.HOURS))
                        .sign(algorithm);
                response.setHeader("Authorization", newToken);
            }
            catch (JWTCreationException | ResourceNotFoundException exception)
            {
                // Invalid Signing configuration / Couldn't convert Claims.
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
    {
        String servletPath = request.getServletPath();
        return !servletPath.equals("/auth/refresh-token");
    }

}
