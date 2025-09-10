package edu.itvo.ejercicio4

data class Guest(
    val name: String,
    val dni: String,
    val reservationHistory: MutableList<Reservation>
)