package ru.raif.delivery.integration.adapters

import arrow.core.flatMap
import org.assertj.core.api.Assertions.assertThat
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.utils.IntegrationTest
import java.util.UUID
import kotlin.test.Test

class OrderRepositoryImplTest : IntegrationTest() {

    @Test
    fun `should save order`() {
        //given
        val order = Location.of(2, 2).flatMap {
            Order.create(
                location = it,
                volume = 8,
            )
        }.getOrNull()!!

        //when
        orderRepository.create(order)

        //then
        val savedOrder = orderRepository.getById(order.id)
        assertThat(savedOrder).isNotNull
    }
}