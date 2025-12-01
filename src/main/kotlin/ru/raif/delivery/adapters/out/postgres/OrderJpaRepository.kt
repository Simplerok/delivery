package ru.raif.delivery.adapters.out.postgres

import org.springframework.data.jpa.repository.JpaRepository
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.core.domain.model.order.OrderStatus
import java.util.*

interface OrderJpaRepository : JpaRepository<Order, UUID> {
    fun findFirstByStatus(status: OrderStatus): Order?
    fun findAllByStatus(status: OrderStatus): List<Order>
}