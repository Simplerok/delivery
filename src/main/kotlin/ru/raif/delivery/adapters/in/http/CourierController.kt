package ru.raif.delivery.adapters.`in`.http

import api.CourierApi
import api.model.CourierCreateDto
import api.model.CourierView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.raif.delivery.core.application.commands.CreateCourierCommandHandler
import ru.raif.delivery.core.application.mapper.asView
import ru.raif.delivery.core.application.queries.GetAllCouriersQuery

@RestController
class CourierController(
    private val createCourierCommandHandler: CreateCourierCommandHandler,
    private val getAllCouriersQuery: GetAllCouriersQuery,
) : CourierApi {
    override fun createCourier(courierCreateDto: CourierCreateDto): ResponseEntity<CourierView> =
        courierCreateDto.asCommand().let { command ->
            createCourierCommandHandler.handle(command)
                .fold(
                    { ResponseEntity.status(HttpStatus.BAD_REQUEST).build() },
                    { ResponseEntity.status(HttpStatus.CREATED).body(it.asView()) },
                )
        }

    override fun getCouriers(): ResponseEntity<List<CourierView>> =
        getAllCouriersQuery.handle()
            .map { it.asView() }
            .let { ResponseEntity.ok(it) }
}