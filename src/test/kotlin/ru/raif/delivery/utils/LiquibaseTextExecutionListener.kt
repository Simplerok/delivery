package ru.raif.delivery.utils

import liquibase.integration.spring.SpringLiquibase
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

class LiquibaseTextExecutionListener: AbstractTestExecutionListener() {

    override fun afterTestExecution(testContext: TestContext) {
        cleanupDB(testContext)
    }

    private fun cleanupDB(testContext: TestContext) {
        val appContext = testContext.applicationContext
        val springLiquibase = appContext.getBean(SpringLiquibase::class.java)
        springLiquibase.isDropFirst = true
        springLiquibase.afterPropertiesSet()
    }
}