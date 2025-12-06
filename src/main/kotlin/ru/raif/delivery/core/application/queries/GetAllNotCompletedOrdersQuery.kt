package ru.raif.delivery.core.application.queries

import ru.raif.delivery.core.application.dto.OrderDto

interface GetAllNotCompletedOrdersQuery {
    fun handle(): List<OrderDto>
}