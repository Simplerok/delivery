package ru.raif.delivery

import arrow.core.Either
import arrow.core.right
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.raif.delivery.core.domain.model.shared.Location

@EnableConfigurationProperties
@SpringBootApplication
class DeliveryApplication

fun main(args: Array<String>) {
    runApplication<DeliveryApplication>(*args)
}
