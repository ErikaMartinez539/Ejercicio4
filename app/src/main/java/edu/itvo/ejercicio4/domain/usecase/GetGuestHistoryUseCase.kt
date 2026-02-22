package edu.itvo.ejercicio4.domain.usecase

import edu.itvo.ejercicio4.domain.repository.IGuestRepository

class GetGuestHistoryUseCase(private val guestRepo: IGuestRepository) {
    fun execute(dni: String) =
        guestRepo.getGuests().find { it.dni == dni }?.reservationHistory ?: emptyList()
}