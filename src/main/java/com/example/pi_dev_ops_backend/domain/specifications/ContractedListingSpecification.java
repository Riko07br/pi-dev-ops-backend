package com.example.pi_dev_ops_backend.domain.specifications;

import com.example.pi_dev_ops_backend.domain.entities.ContractedListing;
import com.example.pi_dev_ops_backend.domain.queryParams.ContractedListingPaginationParams;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ContractedListingSpecification
{
    public static Specification<ContractedListing> filter(ContractedListingPaginationParams contractedListingPaginationParams)
    {
        return (root, query, builder) ->
                builder.and(
                        findById(
                                contractedListingPaginationParams.getMinId(),
                                contractedListingPaginationParams.getMaxId()).toPredicate(root, query, builder),
                        findByStartDate(
                                contractedListingPaginationParams.getMinStartedDate(),
                                contractedListingPaginationParams.getMaxStartedDate()).toPredicate(root, query, builder),
                        findByFinishDate(
                                contractedListingPaginationParams.getMinFinishedDate(),
                                contractedListingPaginationParams.getMaxFinishedDate()).toPredicate(root, query, builder),
                        findByClientId(contractedListingPaginationParams.getClientId()).toPredicate(root, query, builder),
                        findByListingId(contractedListingPaginationParams.getListingId()).toPredicate(root, query, builder)
                );
    }

    private static Specification<ContractedListing> findById(Long minId, Long maxId)
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

    private static Specification<ContractedListing> findByStartDate(String minStartDate, String maxStartDate)
    {
        LocalDate parsedMinStartDate = parseStringToDate(minStartDate, true);
        LocalDate parsedMaxStartDate = parseStringToDate(maxStartDate, true);

        return (root, query, builder) -> {
            if (parsedMinStartDate == null && parsedMaxStartDate == null)
                return builder.conjunction();
            if (parsedMinStartDate != null && parsedMaxStartDate != null)
                return builder.between(root.get("startedAt"), parsedMinStartDate, parsedMaxStartDate);
            if (parsedMinStartDate != null)
                return builder.greaterThanOrEqualTo(root.get("startedAt"), parsedMinStartDate);
            return builder.lessThanOrEqualTo(root.get("startedAt"), parsedMaxStartDate);
        };
    }

    private static Specification<ContractedListing> findByFinishDate(String minFinishedDate, String maxFinishedDate)
    {
        LocalDate parsedMinFinishedDate = parseStringToDate(minFinishedDate, false);
        LocalDate parsedMaxFinishedDate = parseStringToDate(maxFinishedDate, false);

        return (root, query, builder) -> {
            if (parsedMinFinishedDate == null && parsedMaxFinishedDate == null)
                return builder.conjunction();
            if (parsedMinFinishedDate != null && parsedMaxFinishedDate != null)
                return builder.between(root.get("finishedAt"), parsedMinFinishedDate, parsedMaxFinishedDate);
            if (parsedMinFinishedDate != null)
                return builder.greaterThanOrEqualTo(root.get("finishedAt"), parsedMinFinishedDate);
            return builder.lessThanOrEqualTo(root.get("finishedAt"), parsedMaxFinishedDate);
        };
    }

    private static Specification<ContractedListing> findByClientId(Long clientId)
    {
        return (root, query, builder) -> {
            if (clientId == null)
                return builder.conjunction();
            return builder.equal(root.get("client").get("id"), clientId);
        };
    }

    private static Specification<ContractedListing> findByListingId(Long listingId)
    {
        return (root, query, builder) -> {
            if (listingId == null)
                return builder.conjunction();
            return builder.equal(root.get("listing").get("id"), listingId);
        };
    }

    private static LocalDate parseStringToDate(String date, boolean atDayStart)
    {
        return date == null ? null : (atDayStart ?
                LocalDate.parse(date) :
                LocalDate.parse(date).plusDays(1));
    }

}
