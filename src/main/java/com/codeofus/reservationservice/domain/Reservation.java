package com.codeofus.reservationservice.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    Integer personId;

    Integer spotId;

    LocalDateTime createdAt;
    LocalDateTime reservedFrom;
    LocalDateTime reservedUntil;


    public Reservation updateReservation(Reservation reservation) {
        this.personId = reservation.getPersonId();
        this.spotId = reservation.getSpotId();
        this.reservedFrom = reservation.getReservedFrom();
        this.reservedUntil = reservation.getReservedUntil();
        return this;
    }
}
