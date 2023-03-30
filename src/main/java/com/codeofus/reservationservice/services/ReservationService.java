package com.codeofus.reservationservice.services;

import com.codeofus.reservationservice.domain.Reservation;
import com.codeofus.reservationservice.repositories.ReservationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReservationService {

    ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable).getContent();
    }

    public Optional<Reservation> getReservation(int id) {
        return reservationRepository.findById(id);
    }

    @Transactional
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Optional<Reservation> updateReservation(Reservation reservation) {
        Optional<Reservation> reservationToUpdate = reservationRepository.findById(reservation.getId());
        return reservationToUpdate.map(r -> r.updateReservation(reservation));
    }

    @Transactional
    public void deleteReservation(int reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
