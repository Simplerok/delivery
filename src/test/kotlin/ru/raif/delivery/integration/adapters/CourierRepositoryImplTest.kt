package ru.raif.delivery.integration.adapters

import arrow.core.flatMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import ru.raif.delivery.core.domain.model.courier.Courier
import ru.raif.delivery.core.domain.model.shared.Location
import ru.raif.delivery.utils.IntegrationTest
import kotlin.test.Test

@Tag("Integration")
@Execution(ExecutionMode.SAME_THREAD)
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
        courierRepository.save(courier)

        //then
        val savedCourier = courierRepository.getById(courier.id)
        assertThat(savedCourier).isNotNull
    }
}