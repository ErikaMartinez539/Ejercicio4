package edu.itvo.ejercicio4.domain.model

data class Room(
    val number: Int,
    val price: Double,
    val available: Boolean,
    val roomType: RoomType
)

enum class RoomType {
    SINGLE, DOUBLE, SUITE
}