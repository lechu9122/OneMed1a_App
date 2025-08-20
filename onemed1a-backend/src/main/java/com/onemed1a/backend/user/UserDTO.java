package com.onemed1a.backend.user;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.onemed1a.backend.user.User.Gender;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDTO {
    Long id;
    String firstName;
    String lastName;
    String email;
    Gender gender;
    LocalDate dateOfBirth;
    boolean active;
    OffsetDateTime createdAt;
}
