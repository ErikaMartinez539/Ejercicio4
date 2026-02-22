package edu.itvo.ejercicio4.domain.repository

import edu.itvo.ejercicio4.domain.model.Guest

interface IGuestRepository {
    fun getGuests(): List<Guest>
    fun saveGuest(guest: Guest)
    fun removeGuest(guest: Guest)
}