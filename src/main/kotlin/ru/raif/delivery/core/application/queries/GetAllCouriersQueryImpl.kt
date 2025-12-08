package ru.raif.delivery.core.application.queries

import org.springframework.stereotype.Service
import ru.raif.delivery.core.application.dto.CourierDto
import ru.raif.delivery.core.application.mapper.asDto
import ru.raif.delivery.core.ports.CourierRepository

@Service
class GetAllCouriersQueryImpl(
    private val courierRepository: CourierRepository,
):GetAllCouriersQuery {
    override fun handle(): List<CourierDto> =
        courierRepository.findAllBusy().map { it.asDto() }
}