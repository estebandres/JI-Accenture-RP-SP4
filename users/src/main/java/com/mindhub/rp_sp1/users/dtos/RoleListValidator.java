package com.mindhub.rp_sp1.users.dtos;

import com.mindhub.rp_sp1.users.models.RoleType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Arrays;

public class RoleListValidator implements ConstraintValidator<ValidRoleList, List<String>> {

    @Override
    public boolean isValid(List<String> roles, ConstraintValidatorContext context) {
        if (roles == null) {
            return true;
        }
        if (roles.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Roles list cannot be empty.")
                    .addConstraintViolation();
            return false; // List cannot be empty
        }
             // Check for duplicates
        long distinctCount = roles.stream().distinct().count();
        if (roles.size() != distinctCount) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Roles list contains duplicates.")
                   .addConstraintViolation();
            return false;
        }

        List<String> validRoleNames = Arrays.stream(RoleType.values())
                .map(Enum::name)
                .toList();
        for (String role : roles) {
            if (!validRoleNames.contains(role)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Invalid role: " + role)
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}

