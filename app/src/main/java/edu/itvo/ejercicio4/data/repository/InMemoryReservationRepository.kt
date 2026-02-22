package edu.itvo.ejercicio4.data.repository

import edu.itvo.ejercicio4.domain.model.Reservation
import edu.itvo.ejercicio4.domain.repository.IReservationRepository

class InMemoryReservationRepository : IReservationRepository {
    private val reservations: MutableList<Reservation> = mutableListOf()

    override fun getReservations(): List<Reservation> = reservations
    override fun saveReservation(reservation: Reservation) { reservations.add(reservation) }
    override fun removeReservation(reservation: Reservation) { reservations.remove(reservation) }
}