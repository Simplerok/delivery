package ru.raif.delivery.core.domain.model.order

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.domain.AbstractAggregateRoot
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.lib.error.Error
import java.util.*

@Entity
@Table(name = "order")
class Order private constructor(
    @Id
    val id: UUID,
    @Embedded
    val location: Location,
    @Column
    val volume: Int,
    @Column
    var status: OrderStatus,

    @Column
    var courierId: UUID? = null,
) : AbstractAggregateRoot<Order>() {
    public override fun domainEvents(): MutableCollection<Any> = super.domainEvents()

    companion object {
        fun create(id: UUID, location: Location, volume: Int): Either<Error, Order> {
            if (volume <= 0) return Error.INVALID_ARGUMENTS.left()

            return Order(
                id = id,
                location = location,
                volume = volume,
                status = OrderStatus.CREATED,
            ).right()
        }
    }

    fun assign(courierId: UUID) {
        this.courierId = courierId
        status = OrderStatus.ASSIGNED
    }

    fun complete():Either<Error, Unit> {
        if (status != OrderStatus.ASSIGNED) return Error.INVALID_ORDER_STATUS.left()
        status = OrderStatus.COMPLETED
        return Unit.right()
    }
}