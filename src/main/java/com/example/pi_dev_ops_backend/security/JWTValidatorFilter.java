package com.example.pi_dev_ops_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTValidatorFilter extends OncePerRequestFilter
{
    private final String jwtSecretKey;

    public JWTValidatorFilter(String jwtSecretKey)
    {
        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String token = request.getHeader("Authorization");
        if (token != null)
        {
            try
            {
                Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("SeriousBizness")
                        .withSubject("User Info")
                        .build();

                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getClaims().get("username").asString();
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            catch (JWTVerificationException exception)
            {
                throw new RuntimeException("Invalid JWT");
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
    {
        return request.getServletPath().equals("/auth/login");
    }
}
