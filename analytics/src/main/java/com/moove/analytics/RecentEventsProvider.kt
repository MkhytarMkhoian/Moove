package com.moove.analytics

import io.github.mkhytarmkhoian.herald.AnalyticsLifecycleService
import io.github.mkhytarmkhoian.herald.AnalyticsOperation
import io.github.mkhytarmkhoian.herald.ConsentService
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.EventTrackerService
import io.github.mkhytarmkhoian.herald.IdentifiableUserService
import io.github.mkhytarmkhoian.herald.Identity
import io.github.mkhytarmkhoian.herald.Property
import io.github.mkhytarmkhoian.herald.PropertyTrackerService
import io.github.mkhytarmkhoian.herald.asString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * A provider Moove wrote itself: keeps the last [capacity] calls in memory for the analytics
 * inspector screen, so what the app sent can be read on the device without Logcat.
 *
 * It is registered with Herald like any vendor, which is the point — Herald does not own its
 * providers, and an in-house one is a class implementing whichever capabilities it wants. This
 * one implements all five, so the inspector also shows consent, identity and lifecycle calls.
 *
 * Contained vendor failures are recorded too, through [recordFailure], which the error reporter
 * calls; they show up in the same timeline as the call that failed.
 */
class RecentEventsProvider(
    private val capacity: Int = 200,
    private val clock: () -> Long = System::currentTimeMillis,
) : EventTrackerService,
    PropertyTrackerService,
    IdentifiableUserService,
    AnalyticsLifecycleService,
    ConsentService {

    sealed interface Entry {
        val at: Long
        val summary: String

        data class Tracked(override val at: Long, val event: Event) : Entry {
            override val summary: String
                get() = "event ${event.name}" + event.parameters.entries
                    .joinToString(prefix = " { ", postfix = " }") { "${it.key} = ${it.value.asString}" }
                    .takeIf { event.parameters.isNotEmpty() }.orEmpty()
        }

        data class PropertySet(override val at: Long, val property: Property) : Entry {
            override val summary: String get() = "property ${property.name} = ${property.value.asString}"
        }

        data class Identified(override val at: Long, val userId: String) : Entry {
            override val summary: String get() = "identify $userId"
        }

        data class Lifecycle(override val at: Long, override val summary: String) : Entry

        data class Failed(
            override val at: Long,
            val provider: String,
            val operation: AnalyticsOperation,
            val failure: Throwable,
        ) : Entry {
            override val summary: String get() = "FAILED $provider on $operation: ${failure.message}"
        }
    }

    private val _entries = MutableStateFlow<List<Entry>>(emptyList())

    /** Newest first. */
    val entries: StateFlow<List<Entry>> = _entries

    override suspend fun track(event: Event) = add(Entry.Tracked(clock(), event))

    override suspend fun set(property: Property) = add(Entry.PropertySet(clock(), property))

    override suspend fun identify(identity: Identity) = add(Entry.Identified(clock(), identity.userId))

    override suspend fun reset() = add(Entry.Lifecycle(clock(), "reset"))

    override suspend fun start() = add(Entry.Lifecycle(clock(), "start"))

    override suspend fun flush() = add(Entry.Lifecycle(clock(), "flush"))

    override suspend fun setEnabled(enabled: Boolean) = add(Entry.Lifecycle(clock(), "enabled $enabled"))

    fun recordFailure(provider: String, operation: AnalyticsOperation, failure: Throwable) =
        add(Entry.Failed(clock(), provider, operation, failure))

    fun clear() = _entries.update { emptyList() }

    private fun add(entry: Entry) = _entries.update { (listOf(entry) + it).take(capacity) }
}
