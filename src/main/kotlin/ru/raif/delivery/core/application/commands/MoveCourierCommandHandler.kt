package ru.raif.delivery.core.application.commands

import arrow.core.Either
import ru.raif.delivery.lib.error.Error

interface MoveCourierCommandHandler {
    fun handle()
}