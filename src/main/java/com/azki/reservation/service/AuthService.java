package com.azki.reservation.service;

import com.azki.reservation.model.AuthRequest;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    String login(@RequestBody AuthRequest request);
}
