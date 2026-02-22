package edu.itvo.ejercicio4.data.repository

import edu.itvo.ejercicio4.domain.model.Guest
import edu.itvo.ejercicio4.domain.repository.IGuestRepository

class InMemoryGuestRepository : IGuestRepository {
    private val guests: MutableList<Guest> = mutableListOf()

    override fun getGuests(): List<Guest> = guests
    override fun saveGuest(guest: Guest) { guests.add(guest) }
    override fun removeGuest(guest: Guest) { guests.remove(guest) }
}