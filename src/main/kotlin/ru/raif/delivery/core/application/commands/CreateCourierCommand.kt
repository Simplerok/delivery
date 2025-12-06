package ru.raif.delivery.core.application.commands

data class CreateCourierCommand(
    val name: String,
    val speed: Int,
)