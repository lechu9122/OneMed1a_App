package com.onemed1a.backend.config;

import com.onemed1a.backend.media.MediaDataRepository;
import com.onemed1a.backend.user.User;
import com.onemed1a.backend.user.UserRepository;
import com.onemed1a.backend.usermediastatus.UserMediaStatus;
import com.onemed1a.backend.usermediastatus.UserMediaStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.UUID;

@Configuration
public class SeedDataConfig {

    private final PasswordEncoder passwordEncoder;

    public SeedDataConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner seedData(
            UserRepository users,
            MediaDataRepository media,
            UserMediaStatusRepository statuses
    ) {
        return args -> {
            // idempotency: if anything exists, skip
            if (users.count() > 0) return;

            // --- USERS ---

            var u1 = users.save(User.builder()
                    .firstName("Ava")
                    .lastName("Wright")
                    .email("ava@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .gender(User.Gender.FEMALE)
                    .dateOfBirth(LocalDate.of(1996, 5, 17))
                    .active(true)
                    .build());

            var u2 = users.save(User.builder()
                    .firstName("Liam")
                    .lastName("Patel")
                    .email("liam@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .gender(User.Gender.MALE)
                    .dateOfBirth(LocalDate.of(1993, 11, 2))
                    .active(true)
                    .build());


        };
    }
}
