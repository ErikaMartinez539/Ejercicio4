package edu.itvo.ejercicio4.domain.repository

import edu.itvo.ejercicio4.domain.model.Room

interface IRoomRepository {
    fun getRooms(): List<Room>
    fun saveRoom(room: Room)
    fun removeRoom(room: Room)
}