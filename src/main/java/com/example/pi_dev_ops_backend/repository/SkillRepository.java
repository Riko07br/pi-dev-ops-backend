package com.example.pi_dev_ops_backend.repository;

import com.example.pi_dev_ops_backend.domain.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long>
{
    Optional<Skill> findByName(String name);
}
