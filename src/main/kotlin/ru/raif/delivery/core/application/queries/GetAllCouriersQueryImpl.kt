package ru.raif.delivery.core.application.queries

import org.springframework.stereotype.Service
import ru.raif.delivery.adapters.out.postgres.CourierRepositoryImpl
import ru.raif.delivery.core.application.dto.CourierDto
import ru.raif.delivery.core.application.mapper.asDto

@Service
class GetAllCouriersQueryImpl(
    private val courierRepository: CourierRepositoryImpl
):GetAllCouriersQuery {
    override fun handle(): List<CourierDto> =
        courierRepository.findAllBusy().map { it.asDto() }
}