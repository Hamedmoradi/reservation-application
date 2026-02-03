package com.azki.reservation;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class AzkiReservationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AzkiReservationApplication.class, args);
    }

}