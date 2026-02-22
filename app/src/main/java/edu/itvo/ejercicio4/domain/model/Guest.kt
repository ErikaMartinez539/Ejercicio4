package edu.itvo.ejercicio4.domain.model

data class Guest(
    val name: String,
    val dni: String,
    val reservationHistory: MutableList<Reservation> = mutableListOf()
)