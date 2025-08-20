package com.onemed1a.backend.user;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired UserRepository repo;

    Long userId;
    String createdEmail;

    @BeforeEach
    void setup() throws Exception {
        createdEmail = "alice+" + UUID.randomUUID() + "@example.com";

        CreateUserDTO body = new CreateUserDTO(
                "Alice", "Ng", createdEmail,
                User.Gender.UNSPECIFIED, LocalDate.of(2001, 7, 15));

        MvcResult result = mvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode json = om.readTree(result.getResponse().getContentAsString());
        userId = json.get("id").asLong();
        assertThat(userId).isNotNull();
    }

    @Test
    void getUserById_returnsCreatedUser() throws Exception {
        mvc.perform(get("/api/v1/users/{id}", userId))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.email").value(createdEmail));
    }

    @Test
    void update_then_delete_flow() throws Exception {
        UpdateUserDTO upd = new UpdateUserDTO();
        upd.setFirstName("Alicia");

        mvc.perform(patch("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(upd)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.firstName").value("Alicia"));

        mvc.perform(delete("/api/v1/users/{id}", userId))
          .andExpect(status().isNoContent());

        User entity = repo.findById(userId).orElseThrow();
        assertThat(entity.isActive()).isFalse();
    }
}
