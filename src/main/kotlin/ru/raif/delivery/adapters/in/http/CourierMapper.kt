package ru.raif.delivery.adapters.`in`.http

import api.model.CourierCreateDto
import ru.raif.delivery.core.application.commands.CreateCourierCommand

fun CourierCreateDto.asCommand() =
    CreateCourierCommand(
        name = name,
        speed = speed,
    )