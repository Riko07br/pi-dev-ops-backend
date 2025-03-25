package com.example.pi_dev_ops_backend.domain.queryParams;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    private String sortBy = "id";
    private String orderBy = "asc";

    public Pageable getPageable(){
        return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(orderBy), sortBy));
    }
}
