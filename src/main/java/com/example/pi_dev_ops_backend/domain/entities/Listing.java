package com.example.pi_dev_ops_backend.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Table (name = "tb_listing")
public class Listing implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String title;
    private Float value;
    private String description;

    public Listing(Long id, String title, Float value, String description)
    {
        this.id = id;
        this.title = title;
        this.value = value;
        this.description = description;
    }
}
