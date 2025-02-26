package com.example.pi_dev_ops_backend.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table (name = "tb_skill")
public class Skill implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Skill(String name)
    {
        this.name = name;
    }

    @Override
    public final boolean equals(Object o)
    {
        if (!(o instanceof Skill skill))
            return false;

        return Objects.equals(id, skill.id) && name.equals(skill.name);
    }

    @Override
    public int hashCode()
    {
        int result = Objects.hashCode(id);
        result = 31 * result + name.hashCode();
        return result;
    }
}
