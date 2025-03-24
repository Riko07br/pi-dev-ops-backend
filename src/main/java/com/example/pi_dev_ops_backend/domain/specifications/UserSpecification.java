package com.example.pi_dev_ops_backend.domain.specifications;

import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.queryParams.UserPaginationParams;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification
{
    public static Specification<User> filter(UserPaginationParams userPaginationParams)
    {
        return (root, query, builder) -> builder.or(
                findByEmail(userPaginationParams.getEmail()).toPredicate(root, query, builder)
        );
    }

    private static Specification<User> findByEmail(String email)
    {
        return (root, query, builder) ->
                email == null ?
                        builder.conjunction() :
                        builder.like(root.get("email"), "%" + email + "%");
    }
}
