package ru.raif.delivery.core.application.dto

import java.util.UUID

data class StoragePlaceDto(
    val name: String,
    val totalVolume: Int,
    val orderId: UUID?,
)