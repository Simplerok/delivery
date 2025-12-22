package ru.raif.delivery.core.domain.model.order.events

import java.util.UUID

data class OrderCompletedDomainEvent(
    val orderId: UUID,
    val courierId: UUID,
)