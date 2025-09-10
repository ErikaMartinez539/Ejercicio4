package edu.itvo.ejercicio4

import kotlin.collections.MutableList

class Hotel {
    val rooms: MutableList<Room> = mutableListOf()
    val reservations: MutableList<Reservation> = mutableListOf()

    val guests: MutableList<Guest> = mutableListOf()
}
