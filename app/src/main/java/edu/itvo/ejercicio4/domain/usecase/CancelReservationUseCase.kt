package edu.itvo.ejercicio4.domain.usecase

import edu.itvo.ejercicio4.domain.repository.IReservationRepository
import java.time.LocalDate

class CancelReservationUseCase(
    private val reservationRepo: IReservationRepository
) {
    fun execute(reservation: edu.itvo.ejercicio4.domain.model.Reservation): Boolean {
        return if (reservationRepo.getReservations().contains(reservation)) {
            reservationRepo.removeReservation(reservation)
            reservation.guest.reservationHistory.remove(reservation)
            true
        } else false
    }
}