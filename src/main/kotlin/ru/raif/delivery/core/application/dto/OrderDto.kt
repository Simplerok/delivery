package ru.raif.delivery.core.application.dto

import java.util.UUID

data class OrderDto(
    val id: UUID,
    val location: LocationDto,
    val volume: Int,
    val status: OrderStatusDto,
    val courierId: UUID?,
    )