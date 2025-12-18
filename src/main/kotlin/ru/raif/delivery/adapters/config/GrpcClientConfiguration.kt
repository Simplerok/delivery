package ru.raif.delivery.adapters.config

import clients.geo.GeoGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(GrpcGeoClientProperties::class)
class GrpcClientConfiguration(
    private val geoClientProperties: GrpcGeoClientProperties,
) {
    @Bean
    fun geoManagedChannel(): ManagedChannel =
        ManagedChannelBuilder
            .forAddress(geoClientProperties.host, geoClientProperties.port)
            .usePlaintext()
            .build()

    @Bean
    fun geoStub(geoManagedChannel: ManagedChannel): GeoGrpc.GeoBlockingStub =
        GeoGrpc.newBlockingStub(geoManagedChannel)
}