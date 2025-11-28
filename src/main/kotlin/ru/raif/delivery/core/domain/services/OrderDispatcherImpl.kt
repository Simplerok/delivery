package ru.raif.delivery.core.domain.services

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.raif.delivery.core.domain.model.courier.Courier
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.lib.error.Error

class OrderDispatcherImpl: OrderDispatcher {
    override fun dispatch(
        order: Order,
        couriers: List<Courier>
    ): Either<Error, Courier> {
        if (couriers.isEmpty()) return Error.NO_COURIERS_FOR_DELIVERY.left()

        return couriers.sortedBy { courier ->
            courier.calculateDeliveryTime(order.location)
        }.firstOrNull { courier ->
            courier.canTakeOrder(order)
        }?.right() ?: Error.NO_COURIERS_FOR_DELIVERY.left()
    }
}