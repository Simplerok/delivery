package ru.raif.delivery.core.ports

import arrow.core.Either
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.lib.error.Error

interface GeoClient {
    fun getLocation(address: String): Either<Error, Location>
}