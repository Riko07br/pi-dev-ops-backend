package com.example.pi_dev_ops_backend.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Table (name = "tb_skill")
public class Skill implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "skills", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<UserProfile> userProfiles = new HashSet<>();

    public Skill(String name)
    {
        this.name = name;
    }
}
