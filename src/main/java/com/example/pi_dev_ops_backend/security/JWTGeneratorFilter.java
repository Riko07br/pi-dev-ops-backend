package com.example.pi_dev_ops_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JWTGeneratorFilter extends OncePerRequestFilter
{
    static final String JWT_SECRET_KEY = "aw0&apom.w5@7&r-+ty6682/-nhQEfyjyilk89tyK+-Igs1[[hdfw223423fRr";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
        {
            try
            {
                Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
                String token = JWT.create()
                        .withIssuer("SeriousBizness")
                        .withSubject("User Info")
                        .withClaim("username", authentication.getName())
                        .withClaim("userId", (Long) getDetail(authentication, "userId"))
                        .withClaim("accountId", (Long) getDetail(authentication, "accountId"))
                        .withIssuedAt(Instant.now())
                        .withExpiresAt(Instant.now().plus(5, ChronoUnit.HOURS))
                        .sign(algorithm);
                response.setHeader("Authorization", token);
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
        return !request.getServletPath().equals("/auth/login");
    }

    private <T> T getDetail(Authentication authentication, String key)
    {
        try
        {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            if (Objects.isNull(details) || details.isEmpty() || !details.containsKey(key) || Objects.isNull(details.get(key)))
            {
                return null;
            }
            return (T) details.get(key);

        }
        catch (Exception e)
        {
            return null;
        }
    }

}
