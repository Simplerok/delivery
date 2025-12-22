package ru.raif.delivery.core.domain.model.order.events

import java.util.*

data class OrderCreatedDomainEvent(
    val orderId: UUID,
)