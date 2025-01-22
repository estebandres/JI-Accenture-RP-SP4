package com.mindhub.rp_sp1.users.dtos;

import com.mindhub.rp_sp1.users.models.RoleType;
import com.mindhub.rp_sp1.users.models.SiteUser;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link SiteUser}
 */
public record PatchSiteUserDTO(
        Long id,
        @Nullable
        @Pattern(regexp = "^\\s*\\S.*$", message = "Username must not be blank.")
        @Size(min = 3, max = 20)
        String username,
        @Email
        String email,
        @ValidRoleList
        List<String> roles) implements Serializable {
}