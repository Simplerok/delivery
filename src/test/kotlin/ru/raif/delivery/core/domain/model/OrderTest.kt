package ru.raif.delivery.core.domain.model

import arrow.core.flatMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.core.domain.model.order.OrderStatus
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.lib.error.Error
import java.util.*

class OrderTest {
    @Test
    fun `should create Order successfully`() {
        //given
        val location = Location.of(2, 2)

        //when
        val order = location.flatMap {
            Order.create(
                id = UUID.randomUUID(),
                location = it,
                volume = 8,
            )
        }

        //then
        assertThat(order.isRight()).isTrue()
        assertThat(order.getOrNull()?.status).isEqualTo(OrderStatus.CREATED)
    }

    @Test
    fun `should get INVALID_ARGUMENTS when create Order with invalid volume`() {
        //given
        val location = Location.of(2, 2)

        //when
        val order = location.flatMap {
            Order.create(
                id = UUID.randomUUID(),
                location = it,
                volume = -1,
            )
        }

        //then
        assertThat(order.isLeft()).isTrue()
        assertThat(order.leftOrNull()).isEqualTo(Error.INVALID_ARGUMENTS)
    }

    @Test
    fun `should assign courier to Order`() {
        //given
        val order = Location.of(2, 2)
            .flatMap {
                Order.create(
                    id = UUID.randomUUID(),
                    location = it,
                    volume = 8,
                )
            }

        //when
        val result = order.map { it.assign(UUID.randomUUID()) }

        //then
        assertThat(result.isRight()).isTrue()
        assertThat(order.getOrNull()?.status).isEqualTo(OrderStatus.ASSIGNED)
    }

    @Test
    fun `should complete Order successfully`() {
        //given
        val order = Location.of(2, 2)
            .flatMap {
                Order.create(
                    id = UUID.randomUUID(),
                    location = it,
                    volume = 8,
                )
            }
        order.map { it.assign(UUID.randomUUID()) }

        //when
        val result = order.flatMap { it.complete() }

        //then
        assertThat(result.isRight()).isTrue()
        assertThat(order.getOrNull()?.status).isEqualTo(OrderStatus.COMPLETED)
    }

    @Test
    fun `should get INVALID_ORDER_STATUS when complete order in CREATED status`() {
        //given
        val order = Location.of(2, 2)
            .flatMap {
                Order.create(
                    id = UUID.randomUUID(),
                    location = it,
                    volume = 8,
                )
            }

        //when
        val result = order.flatMap { it.complete() }

        //then
        assertThat(result.isLeft()).isTrue()
        assertThat(result.leftOrNull()).isEqualTo(Error.INVALID_ORDER_STATUS)
    }

    @Test
    fun `should get INVALID_ORDER_STATUS when complete order`() {
        //given
        val order = Location.of(2, 2)
            .flatMap {
                Order.create(
                    id = UUID.randomUUID(),
                    location = it,
                    volume = 8,
                )
            }
        order.map { it.assign(UUID.randomUUID()) }
        order.map { it.complete() }

        //when
        val result = order.flatMap { it.complete() }

        //then
        assertThat(result.isLeft()).isTrue()
        assertThat(order.getOrNull()?.status).isEqualTo(OrderStatus.COMPLETED)
    }
}