//package com.azki.reservation.integrationTests;
//
//import com.azki.reservation.entity.AvailableSlot;
//import com.azki.reservation.entity.Reservation;
//import com.azki.reservation.entity.User;
//import com.azki.reservation.model.ReservationRequest;
//import com.azki.reservation.model.ReservationResponse;
//import com.azki.reservation.repository.AvailableSlotRepository;
//import com.azki.reservation.repository.ReservationRepository;
//import com.azki.reservation.repository.UserRepository;
//import com.azki.reservation.service.ReservationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.transaction.annotation.Transactional;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.junit.jupiter.TestcontainersExtension;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Testcontainers(disabledWithoutDocker = true)
//@Transactional
//class ReservationServiceIntegrationTest {
//
//    @Container
//    static MySQLContainer<?> mysql =
//            new MySQLContainer<>("mysql:8.4")
//                    .withDatabaseName("testdb")
//                    .withUsername("test")
//                    .withPassword("test");
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", mysql::getJdbcUrl);
//        registry.add("spring.datasource.username", mysql::getUsername);
//        registry.add("spring.datasource.password", mysql::getPassword);
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
//        registry.add("spring.jpa.show-sql", () -> "false");
//    }
//
//    @Autowired
//    private ReservationService reservationService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private AvailableSlotRepository slotRepository;
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    private User user;
//    private AvailableSlot slot;
//
//    @BeforeEach
//    void setUp() {
//        // create user
//        user = new User();
//        user.setUsername("hamed");
//        user.setEmail("hamed@test.com");
//        user.setPassword("H@mor1991");
//        user = userRepository.save(user);
//
//        // create available slot
//        slot = new AvailableSlot();
//        slot.setStartTime(LocalDateTime.now().plusHours(1));
//        slot.setEndTime(LocalDateTime.now().plusHours(2));
//        slot.setReserved(false);
//        slot = slotRepository.save(slot);
//    }
//
//    @Test
//    void reserveNearestSlot_success() {
//        // given
//        ReservationRequest request = new ReservationRequest(user.getId());
//
//        // when
//        ReservationResponse response = reservationService.reserveNearestSlot(request);
//
//        // then
//        assertNotNull(response);
//        assertNotNull(response.reservationId());
//        assertEquals(slot.getId(), response.slotId());
//
//        AvailableSlot reservedSlot =
//                slotRepository.findById(slot.getId()).orElseThrow();
//
//        assertTrue(reservedSlot.isReserved());
//
//        Reservation reservation =
//                reservationRepository.findById(response.reservationId()).orElseThrow();
//
//        assertEquals(user.getId(), reservation.getUser().getId());
//        assertEquals(slot.getId(), reservation.getAvailableSlot().getId());
//    }
//
//    @Test
//    void cancelReservation_success() {
//        // reserve first
//        ReservationRequest request = new ReservationRequest(user.getId());
//        ReservationResponse response = reservationService.reserveNearestSlot(request);
//
//        // when (cancel)
//        reservationService.cancel(user.getId(), response.reservationId());
//
//        // then (slot unlocked)
//        AvailableSlot availableSlot =
//                slotRepository.findById(slot.getId()).orElseThrow();
//
//        assertFalse(availableSlot.isReserved());
//
//        Reservation reservation =
//                reservationRepository.findById(response.reservationId()).orElseThrow();
//
//        assertEquals(user.getId(), reservation.getUser().getId());
//    }
//}
