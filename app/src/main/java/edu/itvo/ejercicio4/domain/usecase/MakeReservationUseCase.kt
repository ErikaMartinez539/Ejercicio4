package edu.itvo.ejercicio4.domain.usecase

import edu.itvo.ejercicio4.domain.model.Reservation
import edu.itvo.ejercicio4.domain.model.Room
import edu.itvo.ejercicio4.domain.repository.IGuestRepository
import edu.itvo.ejercicio4.domain.repository.IRoomRepository
import edu.itvo.ejercicio4.domain.repository.IReservationRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class MakeReservationUseCase(
    private val guestRepo: IGuestRepository,
    private val roomRepo: IRoomRepository,
    private val reservationRepo: IReservationRepository
) {
    fun execute(dni: String, roomNumber: Int, arrivalDate: LocalDate, departureDate: LocalDate): Boolean {
        val guest = guestRepo.getGuests().find { it.dni == dni } ?: return false
        val room = roomRepo.getRooms().find { it.number == roomNumber } ?: return false

        val reservedRooms = reservationRepo.getReservations()
            .filter { it.arrivalDate < departureDate && it.departureDate > arrivalDate }
            .map { it.room.number }

        if (roomNumber in reservedRooms) return false

        val nights = ChronoUnit.DAYS.between(arrivalDate, departureDate)
        val totalAmount = nights * room.price
        val reservation = Reservation(guest, room, arrivalDate, departureDate, totalAmount)

        reservationRepo.saveReservation(reservation)
        guest.reservationHistory.add(reservation)
        return true
    }
}