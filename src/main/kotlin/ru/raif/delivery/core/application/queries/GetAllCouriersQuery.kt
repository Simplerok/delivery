package ru.raif.delivery.core.application.queries

import ru.raif.delivery.core.application.dto.CourierDto

interface GetAllCouriersQuery {
    fun handle(): List<CourierDto>
}