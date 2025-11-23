package ru.raif.delivery.core.domain.model.courier

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import jakarta.persistence.Column
import jakarta.persistence.Entity
import ru.raif.delivery.lib.ddd.BaseEntity
import ru.raif.delivery.lib.error.Error
import java.util.UUID

@Entity
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

    fun store(orderId: UUID, volume: Int): Either<Error, StoragePlace> {
        if (!this.canStore(volume)) return Error.NOT_ENOUGH_VOLUME.left()
        if (this.isOccupied()) return Error.OCCUPIED.left()

        return this.apply {
            this.orderId = orderId
        }.right()
    }
}