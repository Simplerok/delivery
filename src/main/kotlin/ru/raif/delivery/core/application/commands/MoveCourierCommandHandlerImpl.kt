package ru.raif.delivery.core.application.commands

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.raif.delivery.core.ports.CourierRepository
import ru.raif.delivery.core.ports.OrderRepository

@Service
class MoveCourierCommandHandlerImpl(
    val courierRepository: CourierRepository,
    val orderRepository: OrderRepository,
) : MoveCourierCommandHandler {
    @Transactional
    override fun handle() {
        courierRepository.findAllBusy()
            .forEach { courier ->
                courier.storagePlaces
                    .mapNotNull { it.orderId }
                    .forEach { orderId ->
                        orderRepository.getById(orderId)
                            ?.let { order ->
                                courier.moveTo(order.location)
                            }
                    }
            }
    }
}