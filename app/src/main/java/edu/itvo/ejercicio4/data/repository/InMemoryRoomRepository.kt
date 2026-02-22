package edu.itvo.ejercicio4.data.repository

import edu.itvo.ejercicio4.domain.model.Room
import edu.itvo.ejercicio4.domain.repository.IRoomRepository

class InMemoryRoomRepository : IRoomRepository {
    private val rooms: MutableList<Room> = mutableListOf()

    override fun getRooms(): List<Room> = rooms
    override fun saveRoom(room: Room) { rooms.add(room) }
    override fun removeRoom(room: Room) { rooms.remove(room) }
}