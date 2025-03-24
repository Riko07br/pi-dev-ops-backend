package com.example.pi_dev_ops_backend.repository;

import com.example.pi_dev_ops_backend.domain.entities.ContractedListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContractedListingRepository
        extends JpaRepository<ContractedListing, Long>,
        JpaSpecificationExecutor<ContractedListing>
{
}
