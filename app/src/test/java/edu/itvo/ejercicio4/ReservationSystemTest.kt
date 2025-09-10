package edu.itvo.ejercicio4

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class ReservationSystemTest {

    private lateinit var hotel: Hotel
    private lateinit var reservationSystem: ReservationSystem

    @Before
    fun setUp() {
        // Inicializa un nuevo hotel y sistema de reservas antes de cada prueba
        hotel = Hotel()
        reservationSystem = ReservationSystem(hotel)

        // Prepara los datos de prueba
        // Crea habitaciones
        hotel.rooms.add(Room(number = 101, price = 100.0, available = true, roomType = RoomType.SINGLE))
        hotel.rooms.add(Room(number = 102, price = 150.0, available = true, roomType = RoomType.DOUBLE))
        hotel.rooms.add(Room(number = 103, price = 200.0, available = true, roomType = RoomType.SUITE))

        // Crea huéspedes
        hotel.guests.add(Guest("Juan Perez", "12345678A", mutableListOf()))
        hotel.guests.add(Guest("Ana Gomez", "87654321B", mutableListOf()))
    }

    @Test
    fun getAvailableRooms_shouldReturnCorrectRoomsForPeriod() {
        // Arrange
        val initialDate = LocalDate.of(2025, 10, 10)
        val finalDate = LocalDate.of(2025, 10, 15)

        // Act - se realiza una reserva para la habitación 101 en el período
        val juan = hotel.guests.find { it.dni == "12345678A" }
        val room101 = hotel.rooms.find { it.number == 101 }
        if (juan != null && room101 != null) {
            hotel.reservations.add(Reservation(juan, room101, LocalDate.of(2025, 10, 11), LocalDate.of(2025, 10, 14), 300.0))
        }

        val availableRooms = reservationSystem.getAvailableRooms(initialDate, finalDate)

        // Assert
        assertEquals(2, availableRooms.size)
        assertFalse(availableRooms.any { it.number == 101 })
        assertTrue(availableRooms.any { it.number == 102 })
        assertTrue(availableRooms.any { it.number == 103 })
    }

    @Test
    fun isRoomAvailable_shouldReturnTrueWhenRoomIsFree() {
        // Arrange
        val initialDate = LocalDate.of(2025, 11, 1)
        val finalDate = LocalDate.of(2025, 11, 5)

        // Act
        val isAvailable = reservationSystem.isRoomAvailable(101, initialDate, finalDate)

        // Assert
        assertTrue(isAvailable)
    }

    @Test
    fun isRoomAvailable_shouldReturnFalseWhenRoomIsReserved() {
        // Arrange
        val initialDate = LocalDate.of(2025, 11, 1)
        val finalDate = LocalDate.of(2025, 11, 5)
        val juan = hotel.guests.find { it.dni == "12345678A" }
        val room101 = hotel.rooms.find { it.number == 101 }
        if (juan != null && room101 != null) {
            hotel.reservations.add(Reservation(juan, room101, LocalDate.of(2025, 11, 2), LocalDate.of(2025, 11, 4), 200.0))
        }

        // Act
        val isAvailable = reservationSystem.isRoomAvailable(101, initialDate, finalDate)

        // Assert
        assertFalse(isAvailable)
    }

    @Test
    fun makeReservation_shouldReturnTrueAndAddReservationWhenSuccessful() {
        // Arrange
        val guestDni = "12345678A"
        val roomNumber = 102
        val initialDate = LocalDate.of(2025, 12, 1)
        val finalDate = LocalDate.of(2025, 12, 5)

        // Act
        val result = reservationSystem.makeReservation(guestDni, roomNumber, initialDate, finalDate)

        // Assert
        assertTrue(result)
        assertEquals(1, hotel.reservations.size)
        val savedReservation = hotel.reservations.first()
        assertEquals(guestDni, savedReservation.guest.dni)
        assertEquals(roomNumber, savedReservation.room.number)
    }

    @Test
    fun makeReservation_shouldReturnFalseWhenRoomIsNotAvailable() {
        // Arrange
        val guestDni = "12345678A"
        val roomNumber = 101
        val initialDate = LocalDate.of(2025, 12, 1)
        val finalDate = LocalDate.of(2025, 12, 5)

        // Act - se realiza una reserva para la habitación 101 para que no esté disponible
        reservationSystem.makeReservation(guestDni, roomNumber, LocalDate.of(2025, 12, 2), LocalDate.of(2025, 12, 4))

        // Se intenta hacer una nueva reserva para la misma habitación y fecha
        val result = reservationSystem.makeReservation("87654321B", roomNumber, initialDate, finalDate)

        // Assert
        assertFalse(result)
        assertEquals(1, hotel.reservations.size) // No se agregó una segunda reserva
    }

    @Test
    fun cancelReservation_shouldReturnTrueAndRemoveReservation() {
        // Arrange
        val guestDni = "12345678A"
        val roomNumber = 101
        val arrivalDate = LocalDate.of(2025, 10, 20)

        // Agrega una reserva para poder cancelarla
        reservationSystem.makeReservation(guestDni, roomNumber, arrivalDate, LocalDate.of(2025, 10, 25))

        // Act
        val result = reservationSystem.cancelReservation(guestDni, roomNumber, arrivalDate)

        // Assert
        assertTrue(result)
        assertTrue(hotel.reservations.isEmpty())
    }

    @Test
    fun cancelReservation_shouldReturnFalseWhenReservationNotFound() {
        // Arrange
        val guestDni = "12345678A"
        val roomNumber = 101
        val arrivalDate = LocalDate.of(2025, 10, 20)

        // Act - intenta cancelar una reserva que no existe
        val result = reservationSystem.cancelReservation(guestDni, roomNumber, arrivalDate)

        // Assert
        assertFalse(result)
    }
}