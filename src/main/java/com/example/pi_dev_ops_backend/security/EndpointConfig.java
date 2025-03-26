package com.example.pi_dev_ops_backend.security;

public class EndpointConfig
{
    public static String[] PUBLIC_GET_ENDPOINTS = {
            "/listings/**",
            "/evaluations/**",
    };
    public static String[] PUBLIC_POST_ENDPOINTS = {
            "/auth/register"
    };
    public static String[] PUBLIC_ANY_ENDPOINTS = {
            "/h2-console/**"
    };
}
