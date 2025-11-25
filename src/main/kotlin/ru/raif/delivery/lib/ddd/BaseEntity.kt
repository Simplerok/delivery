package ru.raif.delivery.lib.ddd

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity<T>(
    @Id
    @Column(name = "id")
    val id: T
) {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as BaseEntity<*>

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}