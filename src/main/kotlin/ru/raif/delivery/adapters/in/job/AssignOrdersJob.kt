package ru.raif.delivery.adapters.`in`.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import ru.raif.delivery.core.application.commands.AssignOrderToCourierCommandHandler

@Component
class AssignOrdersJob(
    private val assignOrderToCourierCommandHandler: AssignOrderToCourierCommandHandler
): Job {
    override fun execute(p0: JobExecutionContext?) {
        assignOrderToCourierCommandHandler.handle()
    }
}