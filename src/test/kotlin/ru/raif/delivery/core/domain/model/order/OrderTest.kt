package ru.raif.delivery.core.domain.model.order

import arrow.core.flatMap
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.lib.error.Error
import java.util.UUID

class OrderTest {
    @Test
    fun `should create Order successfully`() {
        //given
        val location = Location.of(2, 2)

        //when
        val order = location.flatMap {
            Order.create(
                orderId = UUID.randomUUID(),
                location = it,
                volume = 8,
            )
        }

        //then
        Assertions.assertThat(order.isRight()).isTrue()
        Assertions.assertThat(order.getOrNull()?.status).isEqualTo(OrderStatus.CREATED)
    }

    @Test
    fun `should get INVALID_ARGUMENTS when create Order with invalid volume`() {
        //given
        val location = Location.of(2, 2)

        //when
        val order = location.flatMap {
            Order.create(
                orderId = UUID.randomUUID(),
                location = it,
                volume = -1,
            )
        }

        //then
        Assertions.assertThat(order.isLeft()).isTrue()
        Assertions.assertThat(order.leftOrNull()).isEqualTo(Error.INVALID_ARGUMENTS)
    }

    @Test
    fun `should assign courier to Order`() {
        //given
        val order = Location.of(2, 2)
            .flatMap {
                Order.create(
                    orderId = UUID.randomUUID(),
                    location = it,
                    volume = 8,
                )
            }

        //when
        val result = order.map { it.assign(UUID.randomUUID()) }

        //then
        Assertions.assertThat(result.isRight()).isTrue()
        Assertions.assertThat(order.getOrNull()?.status).isEqualTo(OrderStatus.ASSIGNED)
    }

    @Test
    fun `should complete Order successfully`() {
        //given
        val order = Location.of(2, 2)
            .flatMap {
                Order.create(
                    orderId = UUID.randomUUID(),
                    location = it,
                    volume = 8,
                )
            }
        order.map { it.assign(UUID.randomUUID()) }

        //when
        val result = order.flatMap { it.complete() }

        //then
        Assertions.assertThat(result.isRight()).isTrue()
        Assertions.assertThat(order.getOrNull()?.status).isEqualTo(OrderStatus.COMPLETED)
    }

    @Test
    fun `should get INVALID_ORDER_STATUS when complete order in CREATED status`() {
        //given
        val order = Location.of(2, 2)
            .flatMap {
                Order.create(
                    orderId = UUID.randomUUID(),
                    location = it,
                    volume = 8,
                )
            }

        //when
        val result = order.flatMap { it.complete() }

        //then
        Assertions.assertThat(result.isLeft()).isTrue()
        Assertions.assertThat(result.leftOrNull()).isEqualTo(Error.INVALID_ORDER_STATUS)
    }

    @Test
    fun `should get INVALID_ORDER_STATUS when complete order`() {
        //given
        val order = Location.of(2, 2)
            .flatMap {
                Order.create(
                    orderId = UUID.randomUUID(),
                    location = it,
                    volume = 8,
                )
            }
        order.map { it.assign(UUID.randomUUID()) }
        order.map { it.complete() }

        //when
        val result = order.flatMap { it.complete() }

        //then
        Assertions.assertThat(result.isLeft()).isTrue()
        Assertions.assertThat(order.getOrNull()?.status).isEqualTo(OrderStatus.COMPLETED)
    }
}