package ru.raif.delivery.adapters.`in`.kafka

import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import queues.basket.BasketEventsProto
import ru.raif.delivery.core.application.commands.CreateOrderCommand
import ru.raif.delivery.core.application.commands.CreateOrderCommandHandler
import java.util.UUID

@Component
@KafkaListener(topics = ["\${app.kafka.topics.baskets-events-topic}"])
class BasketListener(
    private val createOrderCommandHandler: CreateOrderCommandHandler,
) {
    @KafkaHandler
    fun listenBasketConfirmedEvent(event: BasketEventsProto.BasketConfirmedIntegrationEvent) {
        createOrderCommandHandler.handle(event.toCommand())
    }

    private fun BasketEventsProto.BasketConfirmedIntegrationEvent.toCommand(
    ): CreateOrderCommand = CreateOrderCommand(
        orderId = UUID.fromString(this.basketId),
        volume = this.volume,
        street = this.address.street,
    )
}