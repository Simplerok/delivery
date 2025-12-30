package ru.raif.delivery.core.application.eventhandlers

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.modulith.events.CompletedEventPublications
import org.springframework.modulith.events.IncompleteEventPublications
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class DomainEventsRepublishJob(
    private val incompleteEvents: IncompleteEventPublications,
    private val completeEvents: CompletedEventPublications,
): Job  {
    override fun execute(p0: JobExecutionContext?) {
        incompleteEvents.resubmitIncompletePublicationsOlderThan(Duration.ofSeconds(60))
    }
}