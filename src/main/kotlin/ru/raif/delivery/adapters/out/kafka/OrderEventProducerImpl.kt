package ru.raif.delivery.adapters.out.kafka

import com.google.protobuf.Timestamp
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import queues.order.OrderEventsProto
import ru.raif.delivery.core.domain.model.order.events.OrderCompletedDomainEvent
import ru.raif.delivery.core.domain.model.order.events.OrderCreatedDomainEvent
import ru.raif.delivery.core.ports.OrderEventProducer
import java.time.Instant
import java.util.*

@Component
class OrderEventProducerImpl(
    @Qualifier("kafkaTemplateForOrderCreated")
    private val kafkaOrderCreatedTemplate: KafkaTemplate<String, OrderEventsProto.OrderCreatedIntegrationEvent>,
    @Qualifier("kafkaTemplateForOrderCompleted")
    private val kafkaOrderCompletedTemplate: KafkaTemplate<String, OrderEventsProto.OrderCompletedIntegrationEvent>,
    @Value("\${app.kafka.topics.orders-events-topic}") private val orderTopic: String,
) : OrderEventProducer {
    override fun publishOrderCreatedEvent(event: OrderCreatedDomainEvent) {
        kafkaOrderCreatedTemplate.send(orderTopic, event.orderId.toString(), event.mapToKafkaEvent())
            .thenAccept {
                logger.info { "Successfully sent order created event: $event" }
            }
    }

    override fun publishOrderCompletedEvent(event: OrderCompletedDomainEvent) {
        kafkaOrderCompletedTemplate.send(orderTopic, event.orderId.toString(), event.mapToKafkaEvent())
            .thenAccept {
                logger.info { "Successfully sent order completed event: $event" }
            }
    }

    private fun OrderCreatedDomainEvent.mapToKafkaEvent(): OrderEventsProto.OrderCreatedIntegrationEvent =
        OrderEventsProto.OrderCreatedIntegrationEvent.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setOrderId(this.orderId.toString())
            .setEventType("CREATED")
            .setOccurredAt(Timestamp.newBuilder().setSeconds(Instant.now().epochSecond))
            .build()

    private fun OrderCompletedDomainEvent.mapToKafkaEvent(): OrderEventsProto.OrderCompletedIntegrationEvent =
        OrderEventsProto.OrderCompletedIntegrationEvent.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setOrderId(this.orderId.toString())
            .setCourierId(this.courierId.toString())
            .setEventType("COMPLETED")
            .setOccurredAt(Timestamp.newBuilder().setSeconds(Instant.now().epochSecond))
            .build()

    companion object {
        val logger = KotlinLogging.logger { }
    }
}