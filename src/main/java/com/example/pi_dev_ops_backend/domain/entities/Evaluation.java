package com.example.pi_dev_ops_backend.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table (name = "tb_evaluation")
public class Evaluation implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private Integer stars;
    private Instant timestamp;

    @OneToOne
    private ContractedListing contractedListing;

    public Evaluation(String comment, Integer stars, Instant timestamp, ContractedListing contractedListing)
    {
        this.comment = comment;
        this.stars = stars;
        this.timestamp = timestamp;
        this.contractedListing = contractedListing;
    }

    @Override
    public final boolean equals(Object o)
    {
        if (!(o instanceof Evaluation listing))
            return false;

        return Objects.equals(id, listing.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }
}
