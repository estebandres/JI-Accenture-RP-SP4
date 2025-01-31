package com.mindhub.rp_sp1.users.dtos;

import com.mindhub.rp_sp1.users.models.RoleType;
import com.mindhub.rp_sp1.users.models.SiteUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class GetUserDTO{
    Long id;
    String username;
    String email;
    List<RoleType> roles;

    public GetUserDTO(SiteUser siteUser) {
        this.id = siteUser.getId();
        this.username = siteUser.getUsername();
        this.email = siteUser.getEmail();
        this.roles = siteUser.getRoles();
    }
}
