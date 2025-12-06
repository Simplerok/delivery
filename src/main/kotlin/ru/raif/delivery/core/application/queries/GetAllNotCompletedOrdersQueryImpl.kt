package ru.raif.delivery.core.application.queries

import org.springframework.stereotype.Service
import ru.raif.delivery.core.application.dto.OrderDto
import ru.raif.delivery.core.application.mapper.asDto
import ru.raif.delivery.core.domain.model.order.OrderStatus
import ru.raif.delivery.core.ports.OrderRepository

@Service
class GetAllNotCompletedOrdersQueryImpl(
    private val repository: OrderRepository,
) : GetAllNotCompletedOrdersQuery {
    override fun handle(): List<OrderDto> =
        repository.findAllByStatusNot(OrderStatus.COMPLETED).map { it.asDto() }
}