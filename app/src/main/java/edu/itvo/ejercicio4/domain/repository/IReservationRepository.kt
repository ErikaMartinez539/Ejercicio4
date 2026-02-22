package edu.itvo.ejercicio4.domain.repository

import edu.itvo.ejercicio4.domain.model.Reservation

interface IReservationRepository {
    fun getReservations(): List<Reservation>
    fun saveReservation(reservation: Reservation)
    fun removeReservation(reservation: Reservation)
}