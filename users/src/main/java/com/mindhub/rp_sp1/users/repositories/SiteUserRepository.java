package com.mindhub.rp_sp1.users.repositories;

import com.mindhub.rp_sp1.users.models.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    SiteUser findByEmail(String email);
}