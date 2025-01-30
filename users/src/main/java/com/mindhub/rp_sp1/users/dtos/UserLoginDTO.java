package com.mindhub.rp_sp1.users.dtos;

import jakarta.validation.constraints.*;

public record UserLoginDTO(
        @NotNull
        @NotBlank
        @Email
        String email,
        @NotNull
        @Size(min = 10, max = 30)
        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
        String password
) {
}
