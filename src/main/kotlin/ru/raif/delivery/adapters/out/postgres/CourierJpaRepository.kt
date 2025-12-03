package ru.raif.delivery.adapters.out.postgres

import org.springframework.data.jpa.repository.JpaRepository
import ru.raif.delivery.core.domain.model.courier.Courier
import java.util.UUID

interface CourierJpaRepository: JpaRepository<Courier, UUID> {
    fun findAllByStoragePlaces_OrderIdIsNull(): List<Courier>
}