package ru.raif.delivery.integration.commands

import arrow.core.flatMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import ru.raif.delivery.core.domain.model.courier.Courier
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.core.domain.model.order.OrderStatus
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.utils.IntegrationTest
import java.util.UUID
import kotlin.test.Test

@Tag("Integration")
@Execution(ExecutionMode.SAME_THREAD)
class AssignOrderToCourierCommandHandlerImplTest: IntegrationTest() {
    @Test
    fun `should assign order to courier`() {
        //given
        val order = Location.of(2, 2).flatMap {
            Order.create(
                orderId = UUID.randomUUID(),
                location = it,
                volume = 8,
            )
        }.getOrNull()!!
        orderRepository.save(order)
        val courier = Location.of(2, 2).flatMap {
            Courier.create(
                name = "Геннадий",
                speed = 4,
                location = it,
            )
        }.getOrNull()!!
        courierRepository.save(courier)

        //when
        assignOrderToCourierCommandHandlerImpl.handle()

        //then
        transactionTemplate.execute {
            val savedOrder = orderRepository.getById(order.id)
            val savedCourier = courierRepository.getById(courier.id)
            assertThat(savedOrder?.courierId).isEqualTo(courier.id)
            assertThat(savedOrder?.status).isEqualTo(OrderStatus.ASSIGNED)
            assertThat(savedCourier?.storagePlaces?.first()?.orderId).isEqualTo(order.id)
        }
    }
}