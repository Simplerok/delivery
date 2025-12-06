package ru.raif.delivery.core.ports

import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.core.domain.model.order.OrderStatus
import java.util.UUID

interface OrderRepository {

    fun save(order: Order):Order

    fun update(order: Order)

    fun getById(orderId: UUID): Order?

    fun findFirstByStatus(status: OrderStatus): Order?

    fun findAllByStatus(status: OrderStatus): List<Order>

    fun findAllByStatusNot(status: OrderStatus): List<Order>
}