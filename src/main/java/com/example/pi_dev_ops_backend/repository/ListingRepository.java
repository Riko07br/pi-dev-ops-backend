package com.example.pi_dev_ops_backend.repository;

import com.example.pi_dev_ops_backend.domain.entities.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long>
{
}
