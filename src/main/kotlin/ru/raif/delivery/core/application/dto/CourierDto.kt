package ru.raif.delivery.core.application.dto

import java.util.UUID

data class CourierDto(
    val id: UUID,
    val name: String,
    val speed: Int,
    val location: LocationDto,
    val storagePlaces: List<StoragePlaceDto>,
)