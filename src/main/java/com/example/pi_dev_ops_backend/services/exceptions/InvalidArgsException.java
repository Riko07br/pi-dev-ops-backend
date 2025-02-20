package com.example.pi_dev_ops_backend.services.exceptions;

import java.io.Serial;

public class InvalidArgsException extends RuntimeException
{
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidArgsException(String message)
    {
        super(message);
    }

    public InvalidArgsException(Class entityClass, String message)
    {
        super("Invalid argument for " + entityClass.getSimpleName() + ". " + message);
    }
}
