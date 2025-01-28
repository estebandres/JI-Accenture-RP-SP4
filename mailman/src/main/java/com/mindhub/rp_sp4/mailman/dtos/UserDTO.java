package com.mindhub.rp_sp4.mailman.dtos;

import java.io.Serializable;
import java.util.List;

public record UserDTO(Long id, String username, String email, List<RoleType> roles) implements Serializable {
}
