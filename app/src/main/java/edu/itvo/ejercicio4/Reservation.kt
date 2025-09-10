package edu.itvo.ejercicio4

import java.time.LocalDate

data class Reservation(
    val guest: Guest,
    val room: Room,
    val arrivalDate: LocalDate,
    val departureDate: LocalDate,
    val total: Double
)