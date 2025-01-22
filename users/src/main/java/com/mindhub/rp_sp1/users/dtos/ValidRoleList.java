package com.mindhub.rp_sp1.users.dtos;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RoleListValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoleList {
    String message() default "Invalid roles list.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}