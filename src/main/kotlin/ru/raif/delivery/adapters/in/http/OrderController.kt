package ru.raif.delivery.adapters.`in`.http

import api.OrderApi
import api.model.OrderView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.raif.delivery.core.application.commands.CreateOrderCommand
import ru.raif.delivery.core.application.commands.CreateOrderCommandHandler
import ru.raif.delivery.core.application.queries.GetAllNotCompletedOrdersQuery
import java.util.*

@RestController
class OrderController(
    private val createOrderCommandHandler: CreateOrderCommandHandler,
    private val getAllNotCompletedOrdersQuery: GetAllNotCompletedOrdersQuery,
) : OrderApi {
    override fun createOrder(): ResponseEntity<OrderView> =
        CreateOrderCommand(
            orderId = UUID.randomUUID(),
            volume = 5,
            street = "Большая Покровская 10"
        ).let { createOrderCommandHandler.handle(it) }
            .fold(
                { ResponseEntity.status(HttpStatus.BAD_REQUEST).build() },
                { ResponseEntity.status(HttpStatus.CREATED).body(it.asView()) },
            )

    override fun getOrders(): ResponseEntity<List<OrderView>> =
        getAllNotCompletedOrdersQuery.handle()
            .map { it.asView() }
            .let { ResponseEntity.ok(it) }
}