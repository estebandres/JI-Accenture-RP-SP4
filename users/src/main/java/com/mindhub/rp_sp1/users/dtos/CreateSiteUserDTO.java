package com.mindhub.rp_sp1.users.dtos;

import com.mindhub.rp_sp1.users.models.RoleType;
import com.mindhub.rp_sp1.users.models.SiteUser;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link SiteUser}
 */
public record CreateSiteUserDTO(
        Long id,
        @NotNull(message = "Username cannot be null")
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,
        @NotNull(message = "Email cannot be null")
        @Email(message = "Email must be valid")
        String email,
        @ValidRoleList
        List<String> roles) implements Serializable {
}