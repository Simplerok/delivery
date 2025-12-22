package ru.raif.delivery.core.ports

import ru.raif.delivery.core.domain.model.order.events.OrderCompletedDomainEvent
import ru.raif.delivery.core.domain.model.order.events.OrderCreatedDomainEvent

interface OrderEventProducer {
    fun publishOrderCreatedEvent(event: OrderCreatedDomainEvent)
    fun publishOrderCompletedEvent(event: OrderCompletedDomainEvent)
}