package com.onemed1a.backend.dto;

import com.onemed1a.backend.entity.User.Gender;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private LocalDate dateOfBirth;
}
