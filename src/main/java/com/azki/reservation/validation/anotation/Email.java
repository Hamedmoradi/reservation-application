package com.azki.reservation.validation.anotation;

import com.azki.reservation.validation.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {
    String message() default "{validation.aspect.custom.email}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
