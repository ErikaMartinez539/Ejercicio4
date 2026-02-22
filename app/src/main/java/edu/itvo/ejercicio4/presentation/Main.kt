package edu.itvo.ejercicio4.presentation

import edu.itvo.ejercicio4.data.repository.*
import edu.itvo.ejercicio4.domain.usecase.*
import java.time.LocalDate

fun main() {
    val guestRepo = InMemoryGuestRepository()
    val roomRepo = InMemoryRoomRepository()
    val reservationRepo = InMemoryReservationRepository()

    val makeReservation = MakeReservationUseCase(guestRepo, roomRepo, reservationRepo)
    val cancelReservation = CancelReservationUseCase(reservationRepo)
    val getAvailableRooms = GetAvailableRoomsUseCase(roomRepo, reservationRepo)
    val getGuestHistory = GetGuestHistoryUseCase(guestRepo)

    // Datos de ejemplo
    val guest = edu.itvo.ejercicio4.domain.model.Guest("Erika", "123456")
    guestRepo.saveGuest(guest)
    val room = edu.itvo.ejercicio4.domain.model.Room(
        101,
        500.0,
        true,
        edu.itvo.ejercicio4.domain.model.RoomType.SINGLE
    )
    roomRepo.saveRoom(room)

    // Hacer reserva
    val success = makeReservation.execute("123456", 101, LocalDate.now(), LocalDate.now().plusDays(2))
    println("Reserva realizada: $success")

    // Mostrar disponibilidad
    val available = getAvailableRooms.execute(LocalDate.now(), LocalDate.now().plusDays(2))
    println("Habitaciones disponibles: ${available.map { it.number }}")

    // Historial
    val history = getGuestHistory.execute("123456")
    println("Historial del huésped: $history")
}