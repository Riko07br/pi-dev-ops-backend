package com.example.pi_dev_ops_backend.domain.queryParams;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ContractedListingPaginationParams extends PaginationParams
{
    private Long minId;
    private Long maxId;
    private String minStartedDate;
    private String maxStartedDate;
    private String minFinishedDate;
    private String maxFinishedDate;
    private Long listingId;
    private Long clientId;
}
