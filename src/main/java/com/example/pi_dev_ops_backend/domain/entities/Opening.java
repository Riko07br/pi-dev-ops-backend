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
@Table (name = "tb_opening")
public class Opening implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String title;
    private String description;
    private String address;
    private String openingType;

    @ManyToMany(cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<Skill> skills = new HashSet<>();

    public Opening(String title, String description, String address, String openingType)
    {
        this.title = title;
        this.description = description;
        this.address = address;
        this.openingType = openingType;
    }

    public void addSkill(Skill skill)
    {
        this.skills.add(skill);
    }
}
