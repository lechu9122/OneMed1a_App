package com.onemed1a.backend.user;

import java.time.LocalDate;

import com.onemed1a.backend.user.User.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Boolean active;
}
