package com.onemed1a.backend.dto;

import com.onemed1a.backend.entity.User.Gender;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
}
