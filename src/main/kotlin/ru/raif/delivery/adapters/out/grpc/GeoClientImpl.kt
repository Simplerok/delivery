package ru.raif.delivery.adapters.out.grpc

import arrow.core.Either
import clients.geo.GeoGrpc
import clients.geo.GeoProto
import org.springframework.stereotype.Component
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.core.ports.GeoClient
import ru.raif.delivery.lib.error.Error

@Component
class GeoClientImpl(
    private val geoStub: GeoGrpc.GeoBlockingStub,
) : GeoClient {
    override fun getLocation(address: String): Either<Error, Location> {
        val request = GeoProto.GetGeolocationRequest.newBuilder().setStreet(address).build()
        return geoStub.getGeolocation(request).location.let {
            Location.of(it.x, it.y)
        }
    }
}