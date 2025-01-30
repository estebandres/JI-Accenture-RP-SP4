package com.mindhub.rp_sp1.users.dtos;

import com.mindhub.rp_sp1.users.models.RoleType;
import com.mindhub.rp_sp1.users.models.SiteUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.mindhub.rp_sp1.users.models.SiteUser}
 */
@Getter
@Setter
@NoArgsConstructor
public class SiteUserDto implements Serializable {
    Long id;
    String username;
    String email;
    List<RoleType> roles;

    public SiteUserDto(SiteUser siteUser) {
        this.id = siteUser.getId();
        this.username = siteUser.getUsername();
        this.email = siteUser.getEmail();
        this.roles = siteUser.getRoles();
    }
}