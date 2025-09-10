package edu.itvo.ejercicio4

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ReservationSystem (val hotel: Hotel) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAvailableRooms(initialDate: LocalDate, finalDate: LocalDate): List<Room>{
        val reservedRoomsInPeriod = hotel.reservations.filter {
            it.arrivalDate >= initialDate &&
                    it.departureDate <= finalDate }.map { it.room }

        return hotel.rooms.filter {!reservedRoomsInPeriod.contains(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkAvailability(initialDate: LocalDate, finalDate: LocalDate): Boolean{
        return getAvailableRooms(initialDate, finalDate).isNotEmpty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isRoomAvailable(roomNumber:Int, initialDate: LocalDate, finalDate: LocalDate): Boolean{
        val room =getAvailableRooms(initialDate, finalDate).filter { it.available && it.number== roomNumber }
        return room.isNotEmpty()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun makeReservation(dni: String, roomNumber: Int, arrivalDate: LocalDate, departureDate: LocalDate): Boolean {
        val guest = hotel.guests.find { it.dni == dni }
        val room = hotel.rooms.find { it.number == roomNumber }

        if (guest == null) {
            println("Huésped con DNI $dni no encontrado.")
            return false
        }

        if (room == null) {
            println("Habitación $roomNumber no existe.")
            return false
        }


        if (!isRoomAvailable(room.number, initialDate = arrivalDate, finalDate= departureDate)) {
            println("La habitación $roomNumber no está disponible del $arrivalDate al $departureDate.")
            return false
        }

        val nights = ChronoUnit.DAYS.between(arrivalDate, departureDate)
        val totalAmount = nights * room.price

        val reservation = Reservation(guest= guest, room= room,
            arrivalDate= arrivalDate, departureDate= departureDate, total = totalAmount)
        hotel.reservations.add(reservation)
        guest.reservationHistory.add(reservation)

        println("Reservacion realizada para ${guest.name} en habitación ${room.number} del $arrivalDate al $departureDate")
        println("Costo total: $${"%.2f".format(totalAmount)}")
        return true
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun displayAvailability(initialDate: LocalDate, finalDate: LocalDate) {
        println("\nHabitaciones disponibles del $initialDate al $finalDate:")
        val availables = getAvailableRooms(initialDate, finalDate)

        if (availables.isEmpty()) {
            println("No hay habitaciones disponibles en ese rango de fechas.")
        } else {
            availables.forEach {
                println("• Habitación ${it.number} (${it.roomType}) - $${it.price}/noche")
            }
        }
    }

    fun cancelReservation(dni: String, roomNumber: Int, arrivalDate: LocalDate): Boolean {
        val reservation = hotel.reservations.find {
            it.guest.dni == dni && it.room.number == roomNumber && it.arrivalDate == arrivalDate
        }

        if (reservation == null) {
            println("Reservacion no encontrada.")
            return false
        }

        hotel.reservations.remove(reservation)
        reservation.guest.reservationHistory.remove(reservation)

        println("Reservacion cancelada para ${reservation.guest.name} en habitación ${reservation.room.number}")
        return true
    }

    fun displayReservation() {
        println("\nReservaciones actuales:")
        if (hotel.reservations.isEmpty()) {
            println("No hay reservaciones.")
        } else {
            hotel.reservations.forEach {
                println("• ${it.guest.name} - Habitación ${it.room.number} del ${it.arrivalDate} al ${it.departureDate} - $${"%.2f".format(it.total)}")
            }
        }
    }

    fun displayGuestHistory(dni: String) {
        val guest = hotel.guests.find { it.dni == dni }

        if (guest == null) {
            println("Huésped con DNI $dni no encontrado.")
            return
        }

        println("\nHistorial de reservaciones de ${guest.name}:")
        if (guest.reservationHistory.isEmpty()) {
            println("No hay reservas registradas.")
        } else {
            guest.reservationHistory.forEach {
                println("• Habitación ${it.room.number} del ${it.arrivalDate} al ${it.departureDate} - $${"%.2f".format(it.total)}")
            }
        }
    }
}
