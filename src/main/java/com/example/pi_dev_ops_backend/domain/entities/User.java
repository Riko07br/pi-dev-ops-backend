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
@Table (name = "tb_user")
public class User implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    public User(Long id, String email, String password)
    {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode()
    {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(email);
        return result;
    }
}