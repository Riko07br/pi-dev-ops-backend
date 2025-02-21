package com.example.pi_dev_ops_backend.repository;

import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>
{
}
