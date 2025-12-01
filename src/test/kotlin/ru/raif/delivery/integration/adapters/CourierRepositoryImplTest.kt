package ru.raif.delivery.integration.adapters

import arrow.core.flatMap
import org.assertj.core.api.Assertions.assertThat
import ru.raif.delivery.core.domain.model.courier.Courier
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.utils.IntegrationTest
import kotlin.test.Test

class CourierRepositoryImplTest: IntegrationTest() {
    @Test
    fun `should save order`() {
        //given
        val courier = Location.of(2, 2).flatMap {
            Courier.create(
                name = "Геннадий",
                speed = 4,
                location = it,
            )
        }.getOrNull()!!

        //when
        courierRepository.create(courier)

        //then
        val savedOrder = courierRepository.getById(courier.id)
        assertThat(savedOrder).isNotNull
    }
}