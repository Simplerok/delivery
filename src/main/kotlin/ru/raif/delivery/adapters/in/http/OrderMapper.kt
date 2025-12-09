package ru.raif.delivery.adapters.`in`.http

import api.model.OrderView
import ru.raif.delivery.core.application.dto.OrderDto
import ru.raif.delivery.core.application.mapper.asView

fun OrderDto.asView() = OrderView(
    id = id,
    location = location.asView()
)