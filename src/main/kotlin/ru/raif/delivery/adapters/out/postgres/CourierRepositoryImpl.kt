package ru.raif.delivery.adapters.out.postgres

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import ru.raif.delivery.core.domain.model.courier.Courier
import ru.raif.delivery.core.ports.CourierRepository
import java.util.UUID

@Repository
class CourierRepositoryImpl(
    private val repository: CourierJpaRepository,
): CourierRepository {
    override fun create(courier: Courier) {
        repository.save(courier)
    }

    override fun update(courier: Courier) {
        repository.save(courier)
    }

    override fun getById(courierId: UUID): Courier? =
        repository.findByIdOrNull(courierId)

    override fun findAllNotBusy(): List<Courier> =
        repository.findAllByStoragePlaces_OrderIdIsNull()
}