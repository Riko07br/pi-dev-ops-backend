package com.example.pi_dev_ops_backend.domain.specifications;

import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import com.example.pi_dev_ops_backend.domain.queryParams.UserProfilePaginationParams;
import org.springframework.data.jpa.domain.Specification;

public class UserProfileSpecification
{
    public static Specification<UserProfile> filter(UserProfilePaginationParams userProfilePaginationParams)
    {

        return (root, query, builder) ->
                builder.and(
                        findByName(userProfilePaginationParams.getName()).toPredicate(root, query, builder),
                        findByAddress(userProfilePaginationParams.getAddress()).toPredicate(root, query, builder),
                        findByTitle(userProfilePaginationParams.getTitle()).toPredicate(root, query, builder),
                        findBySkill(userProfilePaginationParams.getSkill()).toPredicate(root, query, builder)
                );
    }

    private static Specification<UserProfile> findByName(String name)
    {
        return (root, query, builder) ->
                name == null ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("name")), "%" + name + "%");
    }

    private static Specification<UserProfile> findByAddress(String address)
    {
        return (root, query, builder) ->
                address == null ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("address")), "%" + address + "%");
    }

    private static Specification<UserProfile> findByTitle(String title)
    {
        return (root, query, builder) ->
                title == null ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    private static Specification<UserProfile> findBySkill(String skill)
    {
        return (root, query, builder) ->
                skill == null ?
                        builder.conjunction() :
                        builder.like(root.get("skills").get("name"), "%" + skill + "%");
    }
}
