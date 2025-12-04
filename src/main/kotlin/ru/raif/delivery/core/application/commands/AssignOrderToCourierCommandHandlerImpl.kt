package ru.raif.delivery.core.application.commands

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import ru.raif.delivery.adapters.out.postgres.CourierRepositoryImpl
import ru.raif.delivery.adapters.out.postgres.OrderRepositoryImpl
import ru.raif.delivery.core.domain.model.order.OrderStatus

@Service
class AssignOrderToCourierCommandHandlerImpl(
    private val orderRepository: OrderRepositoryImpl,
    private val courierRepository: CourierRepositoryImpl,
    private val transactionTemplate: TransactionTemplate
) : AssignOrderToCourierCommandHandler {
    private val logger: KLogger = KotlinLogging.logger {}

    @Transactional
    override fun handle() {
        courierRepository.findAllNotBusy()
            .forEach { courier ->
                val order = orderRepository.findFirstByStatus(OrderStatus.CREATED)
                if (order == null) return logger.info { "No orders to assign" }

                transactionTemplate.execute {
                    courier.takeOrder(order)
                        .onRight {
                            order.assign(courier.id)
                            orderRepository.save(order)
                            courierRepository.save(courier)
                        }
                }
            }
    }
}