package com.onemed1a.backend.service;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.onemed1a.backend.dto.CreateUserDTO;
import com.onemed1a.backend.dto.UserDTO;
import com.onemed1a.backend.entity.User;
import com.onemed1a.backend.repository.UserRepository;

class UserServiceTest {
    @Mock
    private UserRepository repo;
    @InjectMocks
    private UserService service;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_savesUser() {
        CreateUserDTO dto = new CreateUserDTO("Jane", "Smith", "jane@example.com", User.Gender.FEMALE, LocalDate.of(1990,1,1));
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .gender(dto.getGender())
                .dateOfBirth(dto.getDateOfBirth())
                .build();
        when(repo.save(any(User.class))).thenReturn(user);
        UserDTO result = service.create(dto);
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
        verify(repo).save(any(User.class));
    }
}
