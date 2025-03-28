package com.example.pi_dev_ops_backend.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table (name = "tb_contracted_listing")
public class ContractedListing implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private String clientRequest;
    private Instant startedAt;
    private Instant finishedAt;

    @ManyToOne
    private UserProfile client;
    @ManyToOne
    private Listing listing;
    @OneToOne(mappedBy = "contractedListing", cascade = CascadeType.REMOVE)
    private Evaluation evaluation;

    public ContractedListing(
            String status,
            String clientRequest,
            Instant startedAt,
            Instant finishedAt,
            Listing listing,
            UserProfile client
    )
    {
        this.status = status;
        this.clientRequest = clientRequest;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.listing = listing;
        this.client = client;
    }

    @Override
    public final boolean equals(Object o)
    {
        if (!(o instanceof ContractedListing contractedListing))
            return false;

        return Objects.equals(id, contractedListing.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }
}
