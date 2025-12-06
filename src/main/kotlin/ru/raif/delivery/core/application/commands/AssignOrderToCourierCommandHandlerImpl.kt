package ru.raif.delivery.core.application.commands

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import ru.raif.delivery.core.domain.model.order.OrderStatus
import ru.raif.delivery.core.domain.services.OrderDispatcher
import ru.raif.delivery.core.ports.CourierRepository
import ru.raif.delivery.core.ports.OrderRepository

@Service
class AssignOrderToCourierCommandHandlerImpl(
    private val orderRepository: OrderRepository,
    private val courierRepository: CourierRepository,
    private val transactionTemplate: TransactionTemplate,
    private val orderDispatcher: OrderDispatcher,
) : AssignOrderToCourierCommandHandler {
    private val logger: KLogger = KotlinLogging.logger {}

    @Transactional
    override fun handle() {
        courierRepository.findAllNotBusy()
            .let { couriers ->
                val order = orderRepository.findFirstByStatus(OrderStatus.CREATED)
                if (order == null) return logger.info { "No orders to assign" }

                orderDispatcher.dispatch(order, couriers)
                    .onRight { courier ->
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
}