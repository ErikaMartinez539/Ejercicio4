package edu.itvo.ejercicio4

import edu.itvo.ejercicio4.data.repository.InMemoryGuestRepository
import edu.itvo.ejercicio4.data.repository.InMemoryReservationRepository
import edu.itvo.ejercicio4.data.repository.InMemoryRoomRepository
import edu.itvo.ejercicio4.domain.model.Guest
import edu.itvo.ejercicio4.domain.model.Room
import edu.itvo.ejercicio4.domain.model.RoomType
import edu.itvo.ejercicio4.domain.model.Reservation
import edu.itvo.ejercicio4.domain.usecase.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class ReservationUseCasesTest {

    private lateinit var guestRepo: InMemoryGuestRepository
    private lateinit var roomRepo: InMemoryRoomRepository
    private lateinit var reservationRepo: InMemoryReservationRepository

    private lateinit var makeReservation: MakeReservationUseCase
    private lateinit var cancelReservation: CancelReservationUseCase
    private lateinit var getAvailableRooms: GetAvailableRoomsUseCase
    private lateinit var getGuestHistory: GetGuestHistoryUseCase

    @Before
    fun setUp() {
        guestRepo = InMemoryGuestRepository()
        roomRepo = InMemoryRoomRepository()
        reservationRepo = InMemoryReservationRepository()

        makeReservation = MakeReservationUseCase(guestRepo, roomRepo, reservationRepo)
        cancelReservation = CancelReservationUseCase(reservationRepo)
        getAvailableRooms = GetAvailableRoomsUseCase(roomRepo, reservationRepo)
        getGuestHistory = GetGuestHistoryUseCase(guestRepo)

        // Prepara datos
        guestRepo.saveGuest(Guest("Juan Perez", "12345678A"))
        guestRepo.saveGuest(Guest("Ana Gomez", "87654321B"))

        roomRepo.saveRoom(
            Room(number = 101, price = 100.0, available = true, roomType = RoomType.SINGLE)
        )
        roomRepo.saveRoom(
            Room(number = 102, price = 150.0, available = true, roomType = RoomType.DOUBLE)
        )
        roomRepo.saveRoom(
            Room(number = 103, price = 200.0, available = true, roomType = RoomType.SUITE)
        )
    }

    @Test
    fun getAvailableRooms_shouldReturnCorrectRoomsForPeriod() {
        val initialDate = LocalDate.of(2025, 10, 10)
        val finalDate = LocalDate.of(2025, 10, 15)

        // Reserva la habitación 101 en el período
        makeReservation.execute("12345678A", 101, LocalDate.of(2025, 10, 11), LocalDate.of(2025, 10, 14))

        val availableRooms = getAvailableRooms.execute(initialDate, finalDate)

        assertEquals(2, availableRooms.size)
        assertFalse(availableRooms.any { it.number == 101 })
        assertTrue(availableRooms.any { it.number == 102 })
        assertTrue(availableRooms.any { it.number == 103 })
    }

    @Test
    fun isRoomAvailable_shouldReturnTrueWhenRoomIsFree() {
        val initialDate = LocalDate.of(2025, 11, 1)
        val finalDate = LocalDate.of(2025, 11, 5)

        val availableRooms = getAvailableRooms.execute(initialDate, finalDate)
        val isAvailable = availableRooms.any { it.number == 101 }

        assertTrue(isAvailable)
    }

    @Test
    fun isRoomAvailable_shouldReturnFalseWhenRoomIsReserved() {
        val initialDate = LocalDate.of(2025, 11, 1)
        val finalDate = LocalDate.of(2025, 11, 5)

        makeReservation.execute("12345678A", 101, LocalDate.of(2025, 11, 2), LocalDate.of(2025, 11, 4))

        val availableRooms = getAvailableRooms.execute(initialDate, finalDate)
        val isAvailable = availableRooms.any { it.number == 101 }

        assertFalse(isAvailable)
    }

    @Test
    fun makeReservation_shouldReturnTrueAndAddReservationWhenSuccessful() {
        val result = makeReservation.execute("12345678A", 102, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 5))

        assertTrue(result)
        val reservations = reservationRepo.getReservations()
        assertEquals(1, reservations.size)
        val savedReservation = reservations.first()
        assertEquals("12345678A", savedReservation.guest.dni)
        assertEquals(102, savedReservation.room.number)
    }

    @Test
    fun makeReservation_shouldReturnFalseWhenRoomIsNotAvailable() {
        // Reserva 101 primero
        makeReservation.execute("12345678A", 101, LocalDate.of(2025, 12, 2), LocalDate.of(2025, 12, 4))
        // Intenta reservar mismo rango con otro huésped
        val result = makeReservation.execute("87654321B", 101, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 5))

        assertFalse(result)
        assertEquals(1, reservationRepo.getReservations().size)
    }

    @Test
    fun cancelReservation_shouldReturnTrueAndRemoveReservation() {
        makeReservation.execute("12345678A", 101, LocalDate.of(2025, 10, 20), LocalDate.of(2025, 10, 25))
        val reservation = reservationRepo.getReservations().first()

        val result = cancelReservation.execute(reservation)

        assertTrue(result)
        assertTrue(reservationRepo.getReservations().isEmpty())
    }

    @Test
    fun cancelReservation_shouldReturnFalseWhenReservationNotFound() {
        val fakeReservation = Reservation(
            guest = guestRepo.getGuests().first(),
            room = roomRepo.getRooms().first(),
            arrivalDate = LocalDate.of(2025, 1, 1),
            departureDate = LocalDate.of(2025, 1, 2),
            total = 100.0
        )

        val result = cancelReservation.execute(fakeReservation)
        assertFalse(result)
    }
}