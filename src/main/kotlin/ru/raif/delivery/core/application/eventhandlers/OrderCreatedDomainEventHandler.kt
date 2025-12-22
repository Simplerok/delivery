package ru.raif.delivery.core.application.eventhandlers

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener
import ru.raif.delivery.core.domain.model.order.events.OrderCreatedDomainEvent
import ru.raif.delivery.core.ports.OrderEventProducer

@Service
class OrderCreatedDomainEventHandler(
    private val orderEventProducer: OrderEventProducer,
) {
    @TransactionalEventListener(classes = [OrderCreatedDomainEvent::class])
    fun handleCreatedEvent(event: OrderCreatedDomainEvent) {
        logger.info { "Try publish kafka event for OrderCreatedDomainEvent:$event" }
        orderEventProducer.publishOrderCreatedEvent(event)
    }

    companion object {
        val logger = KotlinLogging.logger { }
    }
}