package com.azki.reservation.controller;

import com.azki.reservation.enums.AppExceptionStatusEnum;
import com.azki.reservation.exception.AvailableSlotNotFoundException;
import com.azki.reservation.exception.ReservationNotFoundException;
import com.azki.reservation.exception.ResourceNotFoundException;
import com.azki.reservation.filter.JwtFilter;
import com.azki.reservation.model.ReservationRequest;
import com.azki.reservation.model.ReservationResponse;
import com.azki.reservation.service.ReservationService;
import com.azki.reservation.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void reserve_success() throws Exception {
        ReservationRequest request = new ReservationRequest(1L);

        ReservationResponse response = new ReservationResponse(
            100L,
            1L,
            LocalDateTime.of(2026, 2, 2, 10, 0),
            LocalDateTime.of(2026, 2, 2, 11, 0));

        when(reservationService.reserveNearestSlot(any(ReservationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/reservations/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.reservationId").value(100))
            .andExpect(jsonPath("$.slotId").value(1L));
        verify(reservationService, times(1)).reserveNearestSlot(any(ReservationRequest.class));
    }

    @Test
    void cancel_success() throws Exception {
        doNothing().when(reservationService).cancel(1L, 100L);

        mockMvc.perform(delete("/api/reservations/{id}/{userId}", 1L, 100L))
            .andExpect(status().isOk());

        verify(reservationService).cancel(1L, 100L);
    }

    @Test
    void reserve_shouldReturn409_whenSlotAlreadyReserved() throws Exception {
        ReservationRequest request = new ReservationRequest(1L);

        when(reservationService.reserveNearestSlot(any()))
                .thenThrow(new AvailableSlotNotFoundException(
                        AppExceptionStatusEnum.SLOT_ALREADY_RESERVED.getStatus(),
                        AppExceptionStatusEnum.SLOT_ALREADY_RESERVED.getMessage()
                ));

        mockMvc.perform(post("/api/reservations/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void cancel_notFound() throws Exception {

        doThrow(new ReservationNotFoundException(
                AppExceptionStatusEnum.RESERVATION_NOT_FOUND.getStatus(),
                AppExceptionStatusEnum.RESERVATION_NOT_FOUND.getMessage()))
                .when(reservationService).cancel(anyLong(), anyLong());

        mockMvc.perform(delete("/api/reservations/{id}/{userId}", 100L, 1L))
                .andExpect(status().isNotFound());
    }

}
