package com.onemed1a.backend.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // --- Public / Admin routes ---
    @PostMapping("/users")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody CreateUserDTO body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(body));
    }

    @GetMapping("/users/{id}")
    public UserDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // --- Current user (/me) routes ---
    @GetMapping("/me")
    public UserDTO getProfile(@RequestHeader("X-User-Id") Long userId) {
        return service.getProfile(userId);
    }

    @PutMapping("/me")
    public UserDTO updateMe(@RequestHeader("X-User-Id") Long userId,
                            @Valid @RequestBody UpdateUserDTO body) {
        return service.updateProfile(userId, body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@RequestHeader("X-User-Id") Long userId) {
        service.deactivate(userId);
    }
}
