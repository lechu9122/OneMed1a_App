package com.onemed1a.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;

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
