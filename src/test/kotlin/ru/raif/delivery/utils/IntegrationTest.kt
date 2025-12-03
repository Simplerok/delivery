package ru.raif.delivery.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.testcontainers.junit.jupiter.Testcontainers
import ru.raif.delivery.adapters.out.postgres.CourierRepositoryImpl
import ru.raif.delivery.adapters.out.postgres.OrderRepositoryImpl

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = [DataSourceInitializer::class])
@TestExecutionListeners(listeners = [LiquibaseTextExecutionListener::class], mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@ActiveProfiles("junit")
@ConfigurationPropertiesScan
abstract class IntegrationTest {
    @Autowired
    protected lateinit var orderRepository: OrderRepositoryImpl

    @Autowired
    protected lateinit var courierRepository: CourierRepositoryImpl
}