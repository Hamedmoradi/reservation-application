package com.azki.reservation.controller;

import com.azki.reservation.model.ReservationRequest;
import com.azki.reservation.model.ReservationResponse;
import com.azki.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reserve")
    public ResponseEntity<ReservationResponse> reserve(@RequestBody ReservationRequest reservationRequest) {
        return ResponseEntity.ok(reservationService.reserveNearestSlot(reservationRequest));
    }

    @DeleteMapping("/{reserveId}/{userId}")
    public void cancel(@PathVariable Long reserveId, @PathVariable Long userId) {
        reservationService.cancel(reserveId, userId);
    }
}
