package com.onemed1a.backend.user;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name", nullable=false)
    private String firstName;

    @Column(name="last_name", nullable=false)
    private String lastName;

    @Column(nullable=false, unique=true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=16)
    @Builder.Default
    private Gender gender = Gender.UNSPECIFIED;

    @Column(name="date_of_birth")
    private LocalDate dateOfBirth;

    @CreationTimestamp
    @Column(name="created_at", nullable=false, updatable=false)
    private OffsetDateTime createdAt;

    @Column(nullable=false)
    @Builder.Default
    private boolean active = true;

    public enum Gender {
        MALE, FEMALE, NON_BINARY, UNSPECIFIED
    }
}
