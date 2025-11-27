package ru.raif.delivery.core.domain.model.courier

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.springframework.data.domain.AbstractAggregateRoot
import ru.raif.delivery.core.domain.model.order.Order
import ru.raif.delivery.core.domain.model.shared.Location
import java.util.UUID
import ru.raif.delivery.lib.error.Error
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Entity
@Table(name = "courier")
class Courier private constructor(
    @Id
    val id: UUID,
    @Column
    val name: String,
    @Column
    val speed: Int,
    @Embedded
    var location: Location,
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(
        foreignKey = ForeignKey(name = "courier_storage_place_id_fk"),
        name = "storage_place_id",
    )
    val storagePlaces: MutableList<StoragePlace>,
) : AbstractAggregateRoot<Courier>() {
    public override fun domainEvents(): MutableCollection<Any> = super.domainEvents()

    companion object {
        fun create(name: String, speed: Int, location: Location): Either<Error, Courier> {
            if (speed <= 0) return Error.INVALID_ARGUMENTS.left()
            if (name.isBlank()) return Error.INVALID_ARGUMENTS.left()

            return StoragePlace.create(
                name = "Сумка",
                totalVolume = 10,
            ).map { sp ->
                Courier(
                    id = UUID.randomUUID(),
                    name = name,
                    speed = speed,
                    location = location,
                    storagePlaces = mutableListOf(sp),
                )
            }
        }
    }

    fun addStorage(name: String, totalVolume: Int): Either<Error, Unit> {
        return StoragePlace.create(name, totalVolume).map { sp ->
            storagePlaces.add(sp)
        }
    }

    private fun findStoragePlace(order: Order): Either<Error, StoragePlace> {
        return storagePlaces.firstOrNull { it.canStore(order.volume) && !it.isOccupied() }
            ?.right() ?: Error.NOT_ENOUGH_VOLUME.left()
    }

    fun canTakeOrder(order: Order): Boolean {
        return findStoragePlace(order).fold(
            ifLeft = { false },
            ifRight = { true },
        )
    }

    fun takeOrder(order: Order): Either<Error, Unit> {
        return findStoragePlace(order).flatMap { it.store(orderId = order.id, volume = order.volume) }
    }

    fun completeOrder(order: Order): Either<Error, Unit> {
        return storagePlaces.firstOrNull { it.orderId == order.id }
            ?.clear(order.id) ?: Error.ORDER_NOT_FOUND.left()
    }

    fun calculateDeliveryTime(location: Location): Double =
        this.location.distanceTo(location)
            .let { it.toDouble() / this.speed }

    fun moveTo(location: Location): Either<Error, Unit> {
        val xDelta = this.location.x - location.x
        val yDelta = this.location.y - location.y
        var cruiseRange = this.speed
        val xStep = max(-cruiseRange, min(xDelta, cruiseRange))
        cruiseRange -= abs(xStep)
        val yStep = max(-cruiseRange, min(yDelta, cruiseRange))

        return Location.of(this.location.x + xStep, this.location.y + yStep)
            .map { newLocation ->
                this.location = newLocation
            }

    }
}