package com.onemed1a.backend.user;

import java.time.LocalDate;

import com.onemed1a.backend.user.User.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreateUserDTO {
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @Email @NotBlank private String email;
    private Gender gender = Gender.UNSPECIFIED;
    private LocalDate dateOfBirth;
}
