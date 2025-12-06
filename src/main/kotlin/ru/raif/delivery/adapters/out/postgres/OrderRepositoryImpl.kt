package ru.raif.delivery.adapters.out.postgres

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.core.domain.model.order.OrderStatus
import ru.raif.delivery.core.ports.OrderRepository
import java.util.UUID

@Repository
class OrderRepositoryImpl(
    private val repository: OrderJpaRepository
): OrderRepository {
    override fun save(order: Order): Order = repository.save(order)

    override fun update(order: Order) {
        repository.save(order)
    }

    override fun getById(orderId: UUID): Order? =
        repository.findByIdOrNull(orderId)

    override fun findFirstByStatus(status: OrderStatus): Order? =
        repository.findFirstByStatus(status)

    override fun findAllByStatus(status: OrderStatus): List<Order> =
        repository.findAllByStatus(status)

    override fun findAllByStatusNot(status: OrderStatus): List<Order> =
        repository.findAllByStatusNot(status)
}