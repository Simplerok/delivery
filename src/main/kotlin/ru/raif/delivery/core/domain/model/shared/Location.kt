package ru.raif.delivery.core.domain.model.shared

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.raif.delivery.lib.Error
import kotlin.math.abs


data class Location private constructor(
    val x: Int,
    val y: Int,
) {
    fun distanceT0(other: Location): Int =
        abs(x - other.x) + abs(y - other.y)

    companion object {
        fun of(x: Int, y: Int): Either<Error, Location> {
            return if (x !in VALID_RANGE || y !in VALID_RANGE) {
                Error.INVALID_ARGUMENTS.left()
            } else {
                Location(x, y).right()
            }
        }

        private val VALID_RANGE = 1..10
    }
}