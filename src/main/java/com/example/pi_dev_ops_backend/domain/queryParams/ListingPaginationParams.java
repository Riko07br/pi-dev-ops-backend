package com.example.pi_dev_ops_backend.domain.queryParams;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ListingPaginationParams extends PaginationParams
{
    private Long minId;
    private Long maxId;
    private String title;
    private Float minPrice;
    private Float maxPrice;
    private Long accountId;
    private String accountName;
}
