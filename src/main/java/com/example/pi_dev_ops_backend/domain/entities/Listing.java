package com.example.pi_dev_ops_backend.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table (name = "tb_listing")
public class Listing implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Float price;
    private String description;
    private String location;
    private LocalDate creationDate;

    @ManyToOne
    private UserProfile userProfile;
    @OneToMany (mappedBy = "listing")
    @Setter(AccessLevel.NONE)
    private List<ContractedListing> contractedListings = new ArrayList<>();
    @ManyToMany (cascade = CascadeType.REMOVE)
    @Setter(AccessLevel.NONE)
    private Set<Skill> skills = new HashSet<>();

    public Listing(
            String title,
            Float price,
            String description,
            String location,
            LocalDate creationDate,
            UserProfile userProfile
    )
    {
        this.title = title;
        this.price = price;
        this.description = description;
        this.location = location;
        this.creationDate = creationDate;
        this.userProfile = userProfile;
    }

    @Override
    public final boolean equals(Object o)
    {
        if (!(o instanceof Listing listing))
            return false;

        return Objects.equals(id, listing.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }

    public void addSkill(Skill skill)
    {
        this.skills.add(skill);
    }
}
