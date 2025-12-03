package ru.raif.delivery.core.ports

import ru.raif.delivery.core.domain.model.courier.Courier
import java.util.UUID

interface CourierRepository {

    fun create(courier: Courier)

    fun update(courier: Courier)

    fun getById(courierId: UUID): Courier?

    fun findAllNotBusy():List<Courier>

}