package com.onemed1a.backend.controller;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onemed1a.backend.dto.CreateUserDTO;
import com.onemed1a.backend.dto.UpdateUserDTO;
import com.onemed1a.backend.entity.User.Gender;
import com.onemed1a.backend.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired UserRepository repo;

    Long userId;
    String createdEmail;   // <- keep for later assertions

    @BeforeEach
    void setup() throws Exception {
        createdEmail = "alice+" + java.util.UUID.randomUUID() + "@example.com"; // UNIQUE
        var body = new CreateUserDTO("Alice", "Ng", createdEmail,
                Gender.UNSPECIFIED, LocalDate.of(2001, 7, 15));

        var result = mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body)))
            .andExpect(status().isCreated())
            .andReturn();

        var json = om.readTree(result.getResponse().getContentAsString());
        userId = json.get("id").asLong();
        assertThat(userId).isNotNull();
    }

    @Test
    void getUserById_works() throws Exception {
        mvc.perform(get("/api/v1/users/{id}", userId))
        .andExpect(status().isOk())
           .andExpect(jsonPath("$.email").value(createdEmail)); // <- use unique email
    }

    @Test
    void me_get_put_delete_flow() throws Exception {
        // GET /me
        mvc.perform(get("/api/v1/me").header("X-User-Id", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Alice"));

        // PUT /me (update firstName)
        var upd = new UpdateUserDTO();
        upd.setFirstName("Alicia");

        mvc.perform(put("/api/v1/me")
                .header("X-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alicia"));

        // DELETE /me
        mvc.perform(delete("/api/v1/me").header("X-User-Id", userId)).andExpect(status().isNoContent());

        // Verify deactivated in DB
        var entity = repo.findById(userId).orElseThrow();
        assertThat(entity.isActive()).isFalse();
    }
}
