package ru.raif.delivery.core.application.commands

import arrow.core.Either
import arrow.core.left
import org.springframework.stereotype.Service
import ru.raif.delivery.core.application.dto.OrderDto
import ru.raif.delivery.core.application.mapper.asDto
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.core.ports.OrderRepository
import ru.raif.delivery.lib.error.Error

@Service
class CreateOrderCommandHandlerImpl(
    val orderRepository: OrderRepository,
) : CreateOrderCommandHandler {
    override fun handle(command: CreateOrderCommand): Either<Error, OrderDto> {
        if (orderRepository.getById(command.orderId) != null) return Error.ORDER_ALREADY_EXISTS.left()

        return Order.create(
            orderId = command.orderId,
            location = Location.of(1, 2).getOrNull()!!,
            volume = command.volume,
            ).map { orderRepository.save(it).asDto() }
    }
}