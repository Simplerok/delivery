package ru.raif.delivery.adapters.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.geo.grpc")
data class GrpcGeoClientProperties(
    val host: String,
    val port: Int,
)