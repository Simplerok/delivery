package ru.raif.delivery.core.application.mapper

import api.model.CourierView
import ru.raif.delivery.core.application.dto.CourierDto
import ru.raif.delivery.core.application.dto.StoragePlaceDto
import ru.raif.delivery.core.domain.model.courier.Courier
import ru.raif.delivery.core.domain.model.courier.StoragePlace

fun Courier.asDto(): CourierDto = CourierDto(
    id = id,
    name = name,
    speed = speed,
    location = location.asDto(),
    storagePlaces = storagePlaces.map { it.asDto() }
)

fun StoragePlace.asDto(): StoragePlaceDto = StoragePlaceDto(
    name = name,
    totalVolume = totalVolume,
    orderId = orderId,
)

fun CourierDto.asView(): CourierView = CourierView(
    id = id,
    name = name,
    location = location.asView(),
)