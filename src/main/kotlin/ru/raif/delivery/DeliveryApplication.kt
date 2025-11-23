package ru.raif.delivery

import arrow.core.Either
import arrow.core.right
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.raif.delivery.core.domain.model.shared.Location

@SpringBootApplication
class DeliveryApplication

fun main(args: Array<String>) {
    runApplication<DeliveryApplication>(*args)
}
