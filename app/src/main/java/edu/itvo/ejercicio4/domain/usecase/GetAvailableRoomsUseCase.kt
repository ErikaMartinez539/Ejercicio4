package edu.itvo.ejercicio4.domain.usecase

import edu.itvo.ejercicio4.domain.model.Room
import edu.itvo.ejercicio4.domain.repository.IRoomRepository
import edu.itvo.ejercicio4.domain.repository.IReservationRepository
import java.time.LocalDate

class GetAvailableRoomsUseCase(
    private val roomRepo: IRoomRepository,
    private val reservationRepo: IReservationRepository
) {
    fun execute(arrivalDate: LocalDate, departureDate: LocalDate): List<Room> {
        val reservedRooms = reservationRepo.getReservations()
            .filter { it.arrivalDate < departureDate && it.departureDate > arrivalDate }
            .map { it.room.number }

        return roomRepo.getRooms().filter { it.number !in reservedRooms }
    }
}