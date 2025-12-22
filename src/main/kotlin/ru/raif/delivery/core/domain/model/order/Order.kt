package ru.raif.delivery.core.domain.model.order

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import ru.raif.delivery.core.domain.model.order.events.OrderCompletedDomainEvent
import ru.raif.delivery.core.domain.model.order.events.OrderCreatedDomainEvent
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.lib.ddd.Aggregate
import ru.raif.delivery.lib.error.Error
import java.util.*

@Entity
@Table(name = "c_order")
class Order private constructor(
    @Id
    val id: UUID,
    @Embedded
    val location: Location,
    @Column
    val volume: Int,
    @Column
    @Enumerated(EnumType.STRING)
    var status: OrderStatus,
    @Column
    var courierId: UUID? = null,
) : Aggregate<Order>() {
    public override fun domainEvents(): MutableCollection<Any> = super.domainEvents()

    init {
        OrderCreatedDomainEvent(orderId = id)
            .run(::registerEvent)
    }

    companion object {
        fun create(orderId: UUID, location: Location, volume: Int): Either<Error, Order> {
            if (volume <= 0) return Error.INVALID_ARGUMENTS.left()

            return Order(
                id = orderId,
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

    fun complete(): Either<Error, Unit> {
        if (status != OrderStatus.ASSIGNED) return Error.INVALID_ORDER_STATUS.left()
        status = OrderStatus.COMPLETED
        this.courierId?.let {
            OrderCompletedDomainEvent(
                orderId = this.id,
                courierId = it,
            )
        }?.run(::registerEvent)

        return Unit.right()
    }
}