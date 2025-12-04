package ru.raif.delivery.core.application.commands

import arrow.core.Either
import org.springframework.stereotype.Service
import ru.raif.delivery.adapters.out.postgres.CourierRepositoryImpl
import ru.raif.delivery.core.application.dto.CourierDto
import ru.raif.delivery.core.application.mapper.asDto
import ru.raif.delivery.core.domain.model.courier.Courier
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.lib.error.Error

@Service
class CreateCourierCommandHandlerImpl(
    val courierRepository: CourierRepositoryImpl,
) : CreateCourierCommandHandler {
    override fun handle(command: CreateCourierCommand): Either<Error, CourierDto> {
        return Courier.create(
            name = command.name,
            speed = command.speed,
            location = Location.of(1, 2).getOrNull()!!,
        ).map { courierRepository.save(it).asDto() }
    }
}