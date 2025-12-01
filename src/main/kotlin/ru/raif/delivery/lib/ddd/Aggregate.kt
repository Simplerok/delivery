package ru.raif.delivery.lib.ddd

import org.springframework.data.domain.AbstractAggregateRoot

abstract class Aggregate<T : Aggregate<T>> : AbstractAggregateRoot<T>()
