package ru.raif.delivery.core.application.commands

import java.util.UUID

data class CreateOrderCommand(
    val orderId: UUID,
    val volume: Int,
    val street: String,
)