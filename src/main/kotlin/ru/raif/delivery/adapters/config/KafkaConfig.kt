package ru.raif.delivery.adapters.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import queues.basket.BasketEventsProto
import queues.order.OrderEventsProto

@Configuration
@EnableKafka
class KafkaConfig {
    @Autowired
    private lateinit var kafkaProperties: KafkaProperties

    @Bean
    fun consumerFactory(): ConsumerFactory<String, BasketEventsProto.BasketConfirmedIntegrationEvent> {
        val configs = kafkaProperties.buildConsumerProperties()
        configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java

        return DefaultKafkaConsumerFactory(configs)
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, BasketEventsProto.BasketConfirmedIntegrationEvent>
    ): ConcurrentKafkaListenerContainerFactory<String, BasketEventsProto.BasketConfirmedIntegrationEvent> =
        ConcurrentKafkaListenerContainerFactory<String, BasketEventsProto.BasketConfirmedIntegrationEvent>().apply {
            this.consumerFactory = consumerFactory
        }

    @Bean
    fun producerFactoryForOrderCreated(): ProducerFactory<String, OrderEventsProto.OrderCreatedIntegrationEvent> {
        val configs = kafkaProperties.buildProducerProperties()
        configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return DefaultKafkaProducerFactory(configs)
    }

    @Bean
    fun kafkaTemplateForOrderCreated(
        producerFactoryForOrderCreated: ProducerFactory<String, OrderEventsProto.OrderCreatedIntegrationEvent>
    ) = KafkaTemplate(producerFactoryForOrderCreated)

    @Bean
    fun producerFactoryForOrderCompleted(): ProducerFactory<String, OrderEventsProto.OrderCompletedIntegrationEvent> {
        val configs = kafkaProperties.buildProducerProperties()
        configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return DefaultKafkaProducerFactory(configs)
    }

    @Bean
    fun kafkaTemplateForOrderCompleted(
        producerFactoryForOrderCompleted: ProducerFactory<String, OrderEventsProto.OrderCompletedIntegrationEvent>
    ) = KafkaTemplate(producerFactoryForOrderCompleted)
}