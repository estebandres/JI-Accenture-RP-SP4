package com.mindhub.rp_sp1.orders.dtos;

import java.io.Serializable;
import java.util.List;

public record SiteUserDto(Long id, String username, String email, List<String> roles) implements Serializable {
}