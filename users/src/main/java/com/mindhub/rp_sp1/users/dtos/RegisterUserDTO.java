package com.mindhub.rp_sp1.users.dtos;

import com.mindhub.rp_sp1.users.models.RoleType;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.mindhub.todolist.models.AppUser}
 */
public record RegisterUserDTO(
        @NotNull
        @Size(min = 2, max = 15)
        @NotBlank
        String username,
        @NotNull
        @Size(min = 10, max = 30)
        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
        String password,
        @NotNull
        @NotBlank
        @Email
        String email,
        List<RoleType> roles
) implements Serializable {}