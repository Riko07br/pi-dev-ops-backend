package com.example.pi_dev_ops_backend.domain.queryParams;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class PaginationParams implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer page = 0;
    private Integer size = 10;

}
