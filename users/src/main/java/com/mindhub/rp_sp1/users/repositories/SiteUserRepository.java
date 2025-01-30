package com.mindhub.rp_sp1.users.repositories;

import com.mindhub.rp_sp1.users.models.SiteUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByEmail(String email);

    boolean existsAppUserByEmail(String email);
}