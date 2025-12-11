package ru.raif.delivery.core.application.mapper

import api.model.LocationView
import ru.raif.delivery.core.application.dto.LocationDto
import ru.raif.delivery.core.application.dto.OrderDto
import ru.raif.delivery.core.application.dto.OrderStatusDto
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.core.domain.model.order.OrderStatus
import ru.raif.delivery.core.domain.model.shared.Location

fun Order.asDto(): OrderDto = OrderDto(
    id = id,
    location = location.asDto(),
    volume = volume,
    status = status.asDto(),
    courierId = courierId,
)

fun Location.asDto(): LocationDto = LocationDto(
    coordinateX = x,
    coordinateY = y,
)

fun OrderStatus.asDto(): OrderStatusDto = when (this) {
    OrderStatus.ASSIGNED -> OrderStatusDto.ASSIGNED
    OrderStatus.CREATED -> OrderStatusDto.CREATED
    OrderStatus.COMPLETED -> OrderStatusDto.COMPLETED
}

fun LocationDto.asView(): LocationView = LocationView(
    x = coordinateX,
    y = coordinateY,
)