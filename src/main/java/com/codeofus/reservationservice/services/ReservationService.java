package com.codeofus.reservationservice.services;

import com.codeofus.reservationservice.domain.Reservation;
import com.codeofus.reservationservice.errors.BadEntityException;
import com.codeofus.reservationservice.repositories.ReservationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReservationService {

    ReservationRepository reservationRepository;

    public Page<Reservation> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    public Reservation getReservation(int id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new BadEntityException("Reservation does not exist", "reservations", "does-not-exist"));
    }

    @Transactional
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation updateReservation(Reservation reservation) {
        Optional<Reservation> reservationToUpdate = reservationRepository.findById(reservation.getId());
        if (reservationToUpdate.isEmpty()) {
            throw new BadEntityException("Reservation does not exist", "reservations", "id-does-not-exist");
        }
        return reservationToUpdate.get().updateReservation(reservation);
    }

    @Transactional
    public void deleteReservation(int reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
