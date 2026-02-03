//package com.azki.reservation.integrationTests;
//
//import com.azki.reservation.entity.User;
//import com.azki.reservation.exception.UserNotFoundException;
//import com.azki.reservation.model.UserRequest;
//import com.azki.reservation.model.UserResponse;
//import com.azki.reservation.repository.UserRepository;
//import com.azki.reservation.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.transaction.annotation.Transactional;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.util.List;
//
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//import static org.junit.Assert.*;
//
//@SpringBootTest
//@Testcontainers
//@Transactional
//class UserServiceIntegrationTest {
//
//    @Container
//    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
//            .withDatabaseName("testdb")
//            .withUsername("test")
//            .withPassword("test");
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", mysql::getJdbcUrl);
//        registry.add("spring.datasource.username", mysql::getUsername);
//        registry.add("spring.datasource.password", mysql::getPassword);
//
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
//        registry.add("spring.jpa.show-sql", () -> "true");
//    }
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void createUser_success() {
//        // given
//        UserRequest request = new UserRequest("hamed", "hamed@test.com", "H@mor1369");
//
//        // when
//        UserResponse response = userService.createUser(request);
//
//        // then
//        assertNotNull(response.id());
//        assertEquals("hamed", response.username());
//        assertEquals("hamed@test.com", response.email());
//
//        User savedUser = userRepository.findById(response.id()).orElseThrow();
//
//        assertNotEquals("H@mor1369", savedUser.getPassword()); // encoded
//    }
//
//    @Test
//    void getAllUser_success() {
//        userService.createUser(new UserRequest("hamed", "hamed@test.com", "H2mor1369"));
//        userService.createUser(new UserRequest("mary", "mary@test.com", "M@ry5678"));
//
//        // when
//        List<UserResponse> users = userService.getAllUser();
//
//        // then
//        assertEquals(2, users.size());
//    }
//
//    @Test
//    void getUser_success() {
//        UserResponse created = userService.createUser(
//                new UserRequest("hamed", "hamed@test.com", "H@mor1369"));
//
//        // when
//        UserResponse found = userService.getUser(created.id());
//
//        // then
//        assertEquals(created.id(), found.id());
//        assertEquals("hamed", found.username());
//    }
//
//    @Test
//    void getUser_notFound_shouldThrowException() {
//        assertThrows(UserNotFoundException.class, () -> userService.getUser(999L));
//    }
//}
