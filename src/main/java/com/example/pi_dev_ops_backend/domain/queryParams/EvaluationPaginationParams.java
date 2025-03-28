package com.example.pi_dev_ops_backend.domain.queryParams;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class EvaluationPaginationParams extends PaginationParams
{
    private Integer stars;
    private Long clientId;
    private Long listingId;
    private LocalDate startDate;
    private LocalDate endDate;
}
