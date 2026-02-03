package com.azki.reservation.validation;

import com.azki.reservation.enums.AppExceptionStatusEnum;
import com.azki.reservation.exception.ResourceNotFoundException;
import com.azki.reservation.exception.UserNotFoundException;
import com.azki.reservation.validation.anotation.Email;
import jakarta.validation.Valid;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
class TestExceptionController {

    @GetMapping("/resource-not-found")
    public void resourceNotFound() {
        throw new ResourceNotFoundException("404","resource Not Found");
    }

    @PostMapping("/validation")
    public void validation(@Valid @RequestBody TestRequest request) {
    }

    @GetMapping("/unauthorized")
    public void unauthorized() {
        throw new AuthorizationDeniedException("Denied");
    }

    @GetMapping("/generic")
    public void generic() {
        throw new RuntimeException("Error");
    }
}

record TestRequest(@Email String email) {
}
