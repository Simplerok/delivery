package ru.raif.delivery.core.application.commands

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.raif.delivery.adapters.out.postgres.CourierRepositoryImpl
import ru.raif.delivery.adapters.out.postgres.OrderRepositoryImpl

@Service
class MoveCourierCommandHandlerImpl(
    val courierRepository: CourierRepositoryImpl,
    val orderRepository: OrderRepositoryImpl,
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