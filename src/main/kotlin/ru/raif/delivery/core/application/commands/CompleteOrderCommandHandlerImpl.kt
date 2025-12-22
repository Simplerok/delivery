package ru.raif.delivery.core.application.commands

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Service
import ru.raif.delivery.core.application.dto.OrderDto
import ru.raif.delivery.core.application.mapper.asDto
import ru.raif.delivery.core.ports.OrderRepository
import ru.raif.delivery.lib.error.Error
import java.util.UUID

@Service
class CompleteOrderCommandHandlerImpl(
    private val orderRepository: OrderRepository,
) : CompleteOrderCommandHandler {
    override fun handle(orderId: UUID): Either<Error, OrderDto> {
        return orderRepository.getById(orderId)
            ?.let { order ->
                order.complete()
                orderRepository.save(order).asDto()
            }?.right() ?: Error.ORDER_NOT_FOUND.left()
    }
}