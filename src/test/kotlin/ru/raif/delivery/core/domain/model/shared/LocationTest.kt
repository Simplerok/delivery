package ru.raif.delivery.core.domain.model.shared

import arrow.core.Either
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.raif.delivery.lib.Error

class LocationTest {

    @Test
    fun `should create Location successfully`() {
        //given and when
        val location = Location.of(1, 1)

        //then
        assertThat(location.isRight()).isTrue()
    }

    @ParameterizedTest
    @MethodSource("getInvalidLocations")
    fun `should get INVALID_ARGUMENTS when create Location`(location: Either<Error, Location>) {
        //given and when and then
        assertThat(location.isLeft()).isTrue()
        assertThat(location.leftOrNull()).isEqualTo(Error.INVALID_ARGUMENTS)
    }

    @Test
    fun `should calculate distance between two locations`() {
        //given
        val location1 = Location.of(2, 1).getOrNull()!!
        val location2 = Location.of(9, 7).getOrNull()!!

        //when
        val distance = location1.distanceT0(location2)

        //then
        assertThat(distance).isEqualTo(13)
    }

    companion object {
        @JvmStatic
        fun getInvalidLocations(): List<Either<Error, Location>> =
            listOf(
                Location.of(12, 1),
                Location.of(12, 12),
                Location.of(1, 12),
                Location.of(-1, 1),
                Location.of(1, -1),
            )
    }
}