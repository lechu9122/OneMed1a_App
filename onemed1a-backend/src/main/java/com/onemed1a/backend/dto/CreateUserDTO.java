package com.onemed1a.backend.dto;

import com.onemed1a.backend.entity.User.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @Email @NotBlank private String email;
    private Gender gender = Gender.UNSPECIFIED;
    private LocalDate dateOfBirth;
}
