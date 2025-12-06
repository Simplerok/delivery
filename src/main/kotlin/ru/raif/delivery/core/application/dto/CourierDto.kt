package ru.raif.delivery.core.application.dto

data class CourierDto(
    val name: String,
    val speed: Int,
    val location: LocationDto,
    val storagePlaces: List<StoragePlaceDto>,
)