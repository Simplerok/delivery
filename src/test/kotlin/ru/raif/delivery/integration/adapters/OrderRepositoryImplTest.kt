package ru.raif.delivery.integration.adapters

import arrow.core.flatMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.utils.IntegrationTest
import java.util.UUID
import kotlin.test.Test

@Tag("Integration")
@Execution(ExecutionMode.SAME_THREAD)
class OrderRepositoryImplTest : IntegrationTest() {

    @Test
    fun `should save order`() {
        //given
        val order = Location.of(2, 2).flatMap {
            Order.create(
                orderId = UUID.randomUUID(),
                location = it,
                volume = 8,
            )
        }.getOrNull()!!

        //when
        orderRepository.save(order)

        //then
        val savedOrder = orderRepository.getById(order.id)
        assertThat(savedOrder).isNotNull
    }
}