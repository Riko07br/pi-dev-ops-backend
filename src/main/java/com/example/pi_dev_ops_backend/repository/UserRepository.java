package com.example.pi_dev_ops_backend.repository;

import com.example.pi_dev_ops_backend.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByEmail(String email);
}
