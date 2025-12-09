package ru.raif.delivery.adapters.`in`.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import ru.raif.delivery.core.application.commands.MoveCourierCommandHandler

@Component
class MoveCouriersJob(
    private val moveCourierCommandHandler: MoveCourierCommandHandler,
) : Job {
    override fun execute(p0: JobExecutionContext?) {
        moveCourierCommandHandler.handle()
    }
}