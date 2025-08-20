package com.onemed1a.backend.user;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService service;

    @Test
    void create_savesUser() {
        // Arrange
        CreateUserDTO dto = new CreateUserDTO(
                "Jane", "Smith", "jane@example.com",
                User.Gender.FEMALE, LocalDate.of(1990, 1, 1));

        when(repo.existsByEmail("jane@example.com")).thenReturn(false);

        User saved = User.builder()
                .id(1L)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .gender(dto.getGender())
                .dateOfBirth(dto.getDateOfBirth())
                .build();

        when(repo.save(any(User.class))).thenReturn(saved);

        // Act
        UserDTO result = service.create(dto);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
        verify(repo).save(any(User.class));
    }
}
