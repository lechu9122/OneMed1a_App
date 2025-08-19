package com.onemed1a.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.onemed1a.backend.dto.CreateUserDTO;
import com.onemed1a.backend.dto.UpdateUserDTO;
import com.onemed1a.backend.dto.UserDTO;
import com.onemed1a.backend.entity.User;
import com.onemed1a.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository repo;

    public UserDTO create(CreateUserDTO dto) {
        // Optional: surface a friendly 409 instead of raw DB constraint error
        if (repo.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .gender(dto.getGender())
                .dateOfBirth(dto.getDateOfBirth())
                .active(true)
                .build();

        return map(repo.save(user));
    }

    public UserDTO getById(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.isActive()) {
            // Hide inactive users from normal reads
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return map(user);
    }

    public UserDTO getProfile(Long id) {
        return getById(id);
    }

    public UserDTO updateProfile(Long id, UpdateUserDTO dto) {
        User user = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.isActive()) {
            // Do not allow updates to inactive users
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getDateOfBirth() != null) user.setDateOfBirth(dto.getDateOfBirth());

        return map(repo.save(user));
    }

    public void deactivate(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.isActive()) {
            return; // already inactive; treat as idempotent
        }

        user.setActive(false);
        repo.save(user);

        // NOTE: Keep the user's data for a 30-day grace period
        // after deactivation (for recovery/audit). After that, a scheduled job would
        // permanently delete the user's data.
    }

    // ---- mapping ----
    private UserDTO map(User u) {
        return UserDTO.builder()
                .id(u.getId())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .gender(u.getGender())
                .dateOfBirth(u.getDateOfBirth())
                .build();
    }
}
