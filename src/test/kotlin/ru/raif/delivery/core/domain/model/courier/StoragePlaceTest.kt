package ru.raif.delivery.core.domain.model.courier

import arrow.core.flatMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.raif.delivery.lib.error.Error
import java.util.UUID

class StoragePlaceTest {
    @Test
    fun `should create StoragePlace successfully`() {
        //given and when
        val location = StoragePlace.create(
            name = "Легковой авто",
            totalVolume = 10,
        )

        //then
        assertThat(location.isRight()).isTrue()
    }

    @Test
    fun `should get INVALID_ARGUMENTS when create StoragePlace`() {
        //given and when
        val location = StoragePlace.create(
            name = "Легковой авто",
            totalVolume = 0,
        )

        //then
        assertThat(location.isLeft()).isTrue()
        assertThat(location.leftOrNull()).isEqualTo(Error.INVALID_ARGUMENTS)
    }

    @Test
    fun `should store volume successfully`() {
        //given
        val location = StoragePlace.create(
            name = "Легковой авто",
            totalVolume = 10,
        )

        //when
        val result = location.flatMap { it.store(UUID.randomUUID(), 8) }

        //then
        assertThat(result.isRight()).isTrue()
    }

    @Test
    fun `should clear volume successfully`() {
        //given
        val location = StoragePlace.create(
            name = "Легковой авто",
            totalVolume = 10,
        )
        val orderId = UUID.randomUUID()
        location.getOrNull()?.store(orderId, 9)

        //when
        val result = location.flatMap { it.clear(orderId) }

        //then
        assertThat(result.isRight()).isTrue()
        assertThat(location.getOrNull()?.orderId).isNull()
    }

    @Test
    fun `should get NOT_ENOUGH_VOLUME when store volume`() {
        //given and when
        val location = StoragePlace.create(
            name = "Легковой авто",
            totalVolume = 10,
        )

        //when
        val result = location.flatMap { it.store(UUID.randomUUID(), 12) }

        //then
        assertThat(result.isLeft()).isTrue()
        assertThat(result.leftOrNull()).isEqualTo(Error.NOT_ENOUGH_VOLUME)
    }

    @Test
    fun `should get OCCUPIED when store volume`() {
        //given and when
        val location = StoragePlace.create(
            name = "Легковой авто",
            totalVolume = 10,
        )
        location.getOrNull()?.store(UUID.randomUUID(), 9)

        //when
        val result = location.flatMap { it.store(UUID.randomUUID(), 9) }

        //then
        assertThat(result.isLeft()).isTrue()
        assertThat(result.leftOrNull()).isEqualTo(Error.OCCUPIED)
    }

    @Test
    fun `should get ORDER_NOT_FOUND when clear not existing orderId`() {
        //given and when
        val location = StoragePlace.create(
            name = "Легковой авто",
            totalVolume = 10,
        )
        location.getOrNull()?.store(UUID.randomUUID(), 9)

        //when
        val result = location.flatMap { it.clear(UUID.randomUUID()) }

        //then
        assertThat(result.isLeft()).isTrue()
        assertThat(result.leftOrNull()).isEqualTo(Error.ORDER_NOT_FOUND)
    }

}