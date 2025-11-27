package ru.raif.delivery.core.domain.model.courier

import arrow.core.flatMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.core.domain.model.shared.Location
import java.util.*

class CourierTest {
    @Test
    fun `should create Courier successfully`() {
        //given
        val location = Location.of(2, 2)

        //when
        val courier = location.flatMap {
            Courier.create(
                name = "Геннадий",
                speed = 4,
                location = it,
            )
        }

        //then
        assertThat(courier.isRight()).isTrue()
    }

    @Test
    fun `should add storage to Courier successfully`() {
        //given
        val location = Location.of(2, 2)
        val courier = location.flatMap {
            Courier.create(
                name = "Геннадий",
                speed = 4,
                location = it,
            )
        }

        //when
        val result = courier.map {
            it.addStorage(
                name = "Легковой авто",
                totalVolume = 45,
            )
        }

        //then
        assertThat(result.isRight()).isTrue()
        assertThat(courier.getOrNull()?.storagePlaces).hasSize(2)
    }

    @Test
    fun `should define if can take order for Courier successfully`() {
        //given
        val location = Location.of(2, 2)
        val courier = location.flatMap {
            Courier.create(
                name = "Геннадий",
                speed = 4,
                location = it,
            )
        }
        val order = Location.of(2, 2).flatMap {
            Order.create(
                id = UUID.randomUUID(),
                location = it,
                volume = 8,
            )
        }

        //when
        val result = courier.map {
            it.canTakeOrder(order.getOrNull()!!)
        }

        //then
        assertThat(result.isRight()).isTrue()
        assertThat(result.getOrNull()!!).isTrue
    }

    @Test
    fun `should take order for Courier successfully`() {
        //given
        val location = Location.of(2, 2)
        val courier = location.flatMap {
            Courier.create(
                name = "Геннадий",
                speed = 4,
                location = it,
            )
        }
        val order = Location.of(2, 2).flatMap {
            Order.create(
                id = UUID.randomUUID(),
                location = it,
                volume = 8,
            )
        }

        //when
        val result = courier.flatMap {
            it.takeOrder(order.getOrNull()!!)
        }

        //then
        assertThat(result.isRight()).isTrue()
        assertThat(courier.getOrNull()!!.storagePlaces.first().orderId).isNotNull
    }

    @Test
    fun `should complete order for Courier successfully`() {
        //given
        val location = Location.of(2, 2)
        val courier = location.flatMap {
            Courier.create(
                name = "Геннадий",
                speed = 4,
                location = it,
            )
        }
        val order = Location.of(2, 2).flatMap {
            Order.create(
                id = UUID.randomUUID(),
                location = it,
                volume = 8,
            )
        }
        courier.map {
            it.takeOrder(order.getOrNull()!!)
        }

        //when
        val result = courier.flatMap {
            it.completeOrder(order.getOrNull()!!)
        }

        //then
        assertThat(result.isRight()).isTrue()
        assertThat(courier.getOrNull()!!.storagePlaces.first().orderId).isNull()
    }
}