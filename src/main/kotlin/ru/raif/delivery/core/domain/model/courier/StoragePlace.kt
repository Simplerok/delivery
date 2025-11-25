package ru.raif.delivery.core.domain.model.courier

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import ru.raif.delivery.lib.ddd.BaseEntity
import ru.raif.delivery.lib.error.Error
import java.util.UUID

@Entity
@Table(name = "storage_place")
class StoragePlace private constructor(
    id: UUID,
    @Column
    val name: String,
    @Column
    val totalVolume: Int,
    @Column
    var orderId: UUID? = null,
) : BaseEntity<UUID>(id = id) {

    companion object {
        fun create(name: String, totalVolume: Int): Either<Error, StoragePlace> {
            if (name.isBlank()) return Error.INVALID_ARGUMENTS.left()
            if (totalVolume <= 0) return Error.INVALID_ARGUMENTS.left()

            return StoragePlace(
                id = UUID.randomUUID(),
                name = name,
                totalVolume = totalVolume,
            ).right()
        }
    }

    fun canStore(volume: Int): Boolean = totalVolume >= volume

    fun isOccupied(): Boolean = orderId != null

    fun store(orderId: UUID, volume: Int): Either<Error, Unit> {
        if (!this.canStore(volume)) return Error.NOT_ENOUGH_VOLUME.left()
        if (this.isOccupied()) return Error.OCCUPIED.left()

        this.orderId = orderId
        return Unit.right()
    }

    fun clear(orderId: UUID): Either<Error, Unit> {
        if (this.orderId != orderId) return Error.ORDER_NOT_FOUND.left()
        this.orderId = null

        return Unit.right()
    }
}