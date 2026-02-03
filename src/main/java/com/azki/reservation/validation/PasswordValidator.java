package com.azki.reservation.validation;

import com.azki.reservation.validation.anotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final String SPECIAL_CHARACTERS = "^(?=.*[!@#$%^&*()\\-_=+<>?/{}\\[\\]|`~])[a-zA-Z0-9!@#$%^&*()\\-_=+<>?/{}\\[\\]|`~]{13,}$";

    private static final Set<String> WEAK_PASSWORDS = Set.of(
            "password", "123456", "123456789", "qwerty", "abc123", "admin", "letmein", "welcome");

    @Override
    public void initialize(Password constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) {
            return false;
        }
        return isPasswordValid(password);
    }

    private boolean isPasswordValid(String password) {
        boolean result = password.length() >= 8;

        if (password.chars().noneMatch(Character::isUpperCase)) {
            return false;
        }

        if (password.chars().noneMatch(Character::isLowerCase)) {
            return false;
        }

        if (!password.chars().anyMatch(Character::isDigit)) {
            return false;
        }
        if (!password.chars().anyMatch(ch -> SPECIAL_CHARACTERS.indexOf(ch) >= 0)) {
            return false;
        }

        if (WEAK_PASSWORDS.contains(password.toLowerCase())) {
            return false;
        }

        return result;
    }
}
