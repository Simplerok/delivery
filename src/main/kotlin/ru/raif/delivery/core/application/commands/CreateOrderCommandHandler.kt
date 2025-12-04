package ru.raif.delivery.core.application.commands

import arrow.core.Either
import ru.raif.delivery.core.application.dto.OrderDto
import ru.raif.delivery.lib.error.Error

interface CreateOrderCommandHandler {
    fun handle(command: CreateOrderCommand): Either<Error, OrderDto>
}