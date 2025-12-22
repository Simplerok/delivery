package ru.raif.delivery.core.application.eventhandlers

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener
import ru.raif.delivery.core.domain.model.order.events.OrderCompletedDomainEvent
import ru.raif.delivery.core.ports.OrderEventProducer

@Service
class OrderCompletedDomainEventHandler(
    private val orderEventProducer: OrderEventProducer,
) {
    @TransactionalEventListener(classes = [OrderCompletedDomainEvent::class])
    fun handleCreatedEvent(event: OrderCompletedDomainEvent) {
        logger.info { "Try publish kafka event for OrderCompletedDomainEvent:$event" }
        orderEventProducer.publishOrderCompletedEvent(event)
    }

    companion object {
        val logger = KotlinLogging.logger { }
    }
}