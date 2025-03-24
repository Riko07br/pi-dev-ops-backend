package com.example.pi_dev_ops_backend.domain.queryParams;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserProfilePaginationParams extends PaginationParams
{
    private String name;
    private String address;
    private String title;
    private String skill;
}
