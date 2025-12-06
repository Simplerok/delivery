package ru.raif.delivery.core.domain.services

import arrow.core.Either
import ru.raif.delivery.core.domain.model.courier.Courier
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.lib.error.Error

interface OrderDispatcher {
    fun dispatch(order: Order, couriers: List<Courier>): Either<Error, Courier>
}