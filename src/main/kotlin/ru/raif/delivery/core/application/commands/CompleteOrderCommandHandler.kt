package ru.raif.delivery.core.application.commands

import arrow.core.Either
import ru.raif.delivery.core.application.dto.OrderDto
import ru.raif.delivery.lib.error.Error
import java.util.UUID

interface CompleteOrderCommandHandler {
    fun handle(orderId: UUID): Either<Error, OrderDto>
}