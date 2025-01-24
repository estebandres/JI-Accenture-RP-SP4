package com.mindhub.rp_sp1.users.dtos;

import com.mindhub.rp_sp1.users.models.RoleType;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.mindhub.rp_sp1.users.models.SiteUser}
 */
public record SiteUserDto(Long id, String username, String email, List<RoleType> roles) implements Serializable {
}