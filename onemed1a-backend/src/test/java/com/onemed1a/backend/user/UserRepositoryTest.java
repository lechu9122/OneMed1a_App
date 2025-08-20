package com.onemed1a.backend.user;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.onemed1a.backend.user.User.Gender;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Test
    void createAndFindById_andFindByEmail() {
        User toSave = User.builder()
                .firstName("Alice")
                .lastName("Ng")
                .email("alice@example.com")
                .gender(Gender.UNSPECIFIED)
                .dateOfBirth(LocalDate.of(2001, 7, 15))
                .active(true)
                .build();

        User saved = repo.save(toSave);
        assertThat(saved.getId()).isNotNull();

        Optional<User> byId = repo.findById(saved.getId());
        assertThat(byId).isPresent();
        assertThat(byId.get().getEmail()).isEqualTo("alice@example.com");

        Optional<User> byEmail = repo.findByEmail("alice@example.com");
        assertThat(byEmail).isPresent();
        assertThat(byEmail.get().getFirstName()).isEqualTo("Alice");
    }

    @Test
    void emailIsUnique() {
        User u1 = User.builder()
                .firstName("A").lastName("B")
                .email("unique@example.com")
                .gender(Gender.UNSPECIFIED)
                .active(true)
                .build();
        repo.saveAndFlush(u1);

        User u2 = User.builder()
                .firstName("C").lastName("D")
                .email("unique@example.com") // duplicate
                .gender(Gender.UNSPECIFIED)
                .active(true)
                .build();

        try {
            repo.saveAndFlush(u2);
            assertThat(true).as("Expected unique constraint to trigger").isFalse();
        } catch (Exception e) {
            assertThat(e.getMessage()).containsIgnoringCase("constraint");
        }
    }
}
