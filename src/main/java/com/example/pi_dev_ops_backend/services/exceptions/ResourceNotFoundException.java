package com.example.pi_dev_ops_backend.services.exceptions;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException
{
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(Class entityClass, Long id)
    {
        super("Resource of type " + entityClass.getSimpleName() + " not found. Id: " + id);
    }

    public ResourceNotFoundException(Class entityClass, String complement)
    {
        super("Resource of type " + entityClass.getSimpleName() + " not found. " + complement);
    }
}
