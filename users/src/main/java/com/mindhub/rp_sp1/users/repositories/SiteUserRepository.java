package com.mindhub.rp_sp1.users.repositories;

import com.mindhub.rp_sp1.users.models.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByEmail(String email);
}