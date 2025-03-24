package com.example.pi_dev_ops_backend.domain.specifications;

import com.example.pi_dev_ops_backend.domain.entities.Listing;
import com.example.pi_dev_ops_backend.domain.queryParams.ListingPaginationParams;
import org.springframework.data.jpa.domain.Specification;

public class ListingSpecification
{
    public static Specification<Listing> filter(ListingPaginationParams listingPaginationParams)
    {
        return (root, query, builder) ->
                builder.and(
                        findById(
                                listingPaginationParams.getMinId(),
                                listingPaginationParams.getMaxId()).toPredicate(root, query, builder),
                        findByPrice(
                                listingPaginationParams.getMinPrice(),
                                listingPaginationParams.getMaxPrice()).toPredicate(root, query, builder),
                        findByTitle(listingPaginationParams.getTitle()).toPredicate(root, query, builder),
                        findByAccountId(listingPaginationParams.getAccountId()).toPredicate(root, query, builder),
                        findByAccountName(listingPaginationParams.getAccountName()).toPredicate(root, query, builder)
                );
    }

    private static Specification<Listing> findById(Long minId, Long maxId)
    {
        return (root, query, builder) ->
        {
            if (minId == null && maxId == null)
                return builder.conjunction();
            if (minId != null && maxId != null)
                return builder.between(root.get("id"), minId, maxId);
            if (minId != null)
                return builder.greaterThanOrEqualTo(root.get("id"), minId);
            return builder.lessThanOrEqualTo(root.get("id"), maxId);
        };
    }

    private static Specification<Listing> findByTitle(String title)
    {
        return (root, query, builder) ->
        {
            if (title == null)
                return builder.conjunction();
            return builder.like(builder.lower(root.get("title")), "%" + title + "%");
        };
    }

    private static Specification<Listing> findByPrice(Float minPrice, Float maxPrice)
    {
        return (root, query, builder) ->
        {
            if (minPrice == null && maxPrice == null)
                return builder.conjunction();
            if (minPrice != null && maxPrice != null)
                return builder.between(root.get("price"), minPrice, maxPrice);
            if (minPrice != null)
                return builder.greaterThanOrEqualTo(root.get("price"), minPrice);
            return builder.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    private static Specification<Listing> findByAccountId(Long accountId)
    {
        return (root, query, builder) ->
                accountId == null
                        ? builder.conjunction()
                        : builder.equal(root.get("userProfile").get("id"), accountId);

    }

    private static Specification<Listing> findByAccountName(String accountName)
    {
        return (root, query, builder) ->
                accountName == null
                        ? builder.conjunction()
                        : builder.like(builder.lower(root.get("userProfile").get("name")), "%" + accountName + "%");

    }

}
