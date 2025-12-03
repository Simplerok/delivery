package ru.raif.delivery.core.domain.services

import arrow.core.flatMap
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.raif.delivery.core.domain.model.courier.Courier
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.lib.error.Error
import java.util.*

class OrderDispatcherImplTest {
    val orderDispatcher = OrderDispatcherImpl()

    @Test
    fun `should dispatch order to nearest courier`(){
        //given
        val order = Location.of(2, 2)
            .flatMap {
                Order.create(
                    location = it,
                    volume = 8,
                )
            }.getOrNull()!!
        val courier1 = Location.of(6, 6).flatMap {
            Courier.create(
                name = "Геннадий",
                speed = 4,
                location = it,
            )
        }.getOrNull()!!
        val courier2 = Location.of(5, 5).flatMap {
            Courier.create(
                name = "Виталий",
                speed = 4,
                location = it,
            )
        }.getOrNull()!!
        val courier3 = Location.of(8, 8).flatMap {
            Courier.create(
                name = "Иннокентий",
                speed = 2,
                location = it,
            )
        }.getOrNull()!!

        //when
        val result = orderDispatcher.dispatch(order = order, couriers = listOf(courier1, courier2, courier3))

        //then
        Assertions.assertThat(result.isRight()).isTrue()
        Assertions.assertThat(result.getOrNull()?.id).isEqualTo(courier2.id)
    }

    @Test
    fun `should get NO_COURIERS_FOR_DELIVERY when dispatch order`(){
        //given
        val order = Location.of(2, 2)
            .flatMap {
                Order.create(
                    location = it,
                    volume = 12,
                )
            }.getOrNull()!!
        val courier1 = Location.of(6, 6).flatMap {
            Courier.create(
                name = "Геннадий",
                speed = 4,
                location = it,
            )
        }.getOrNull()!!
        val courier2 = Location.of(5, 5).flatMap {
            Courier.create(
                name = "Виталий",
                speed = 4,
                location = it,
            )
        }.getOrNull()!!
        val courier3 = Location.of(8, 8).flatMap {
            Courier.create(
                name = "Иннокентий",
                speed = 2,
                location = it,
            )
        }.getOrNull()!!

        //when
        val result = orderDispatcher.dispatch(order = order, couriers = listOf(courier1, courier2, courier3))

        //then
        Assertions.assertThat(result.isLeft()).isTrue()
        Assertions.assertThat(result.leftOrNull()).isEqualTo(Error.NO_COURIERS_FOR_DELIVERY)
    }
}