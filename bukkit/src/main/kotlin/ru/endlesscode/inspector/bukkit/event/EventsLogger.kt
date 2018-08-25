package ru.endlesscode.inspector.bukkit.event

import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredListener
import ru.endlesscode.inspector.bukkit.util.EventsUtils
import java.util.logging.Logger


internal class EventsLogger(
        val logger: Logger,
        private val rules: Map<String, LogRule>,
        private val showHierarchy: Boolean
) {

    companion object {
        private const val TAG = "[EventsLogger]"
    }

    fun inject(plugin: Plugin) {
        val registeredListener = object : RegisteredListener(
                EventsUtils.NULL_LISTENER,
                EventsUtils.NULL_EXECUTOR,
                EventPriority.MONITOR,
                plugin,
                false
        ) {
            override fun callEvent(event: Event) {
                val logRule = findLogRule(event.javaClass) ?: return

                logRule.onEvent {
                    logEvent(event, logRule.skipped)
                }
            }
        }

        injectToAllEvents(registeredListener)
    }

    private fun findLogRule(eventClass: Class<*>): LogRule? {
        val eventName = eventClass.simpleName
        if (eventName in rules) return rules.getValue(eventName)

        return if (eventClass == Event::class.java) null else findLogRule(eventClass.superclass)
    }

    private fun logEvent(event: Event, skipped: Int) {
        val (hierarchy, details) = EventDetails.forEvent(event)

        val skippedString = if (skipped > 0) " (skipped $skipped)" else ""
        val sb = buildString {
            append("$TAG ${event.eventName}$skippedString\n")

            if (showHierarchy) {
                append("    Hierarchy: ").append(hierarchy).append("\n")
            }

            append("    Fields:\n")
            var prefix = ""
            for (detail in details) {
                append(prefix)
                append("        ")
                append(detail)
                prefix = "\n"
            }
        }

        logger.info(sb)
    }

    private fun injectToAllEvents(registeredListener: RegisteredListener) {
        for (eventClass in EventsUtils.eventsClasses) {
            injectToEvent(eventClass, registeredListener)
        }
    }

    private fun injectToEvent(eventClass: Class<out Event>, registeredListener: RegisteredListener) {
        try {
            val handlerList = EventsUtils.getEventListeners(eventClass)
            handlerList.register(registeredListener)
        } catch (e: Exception) {
            // It's ok. We just ignore it
        }
    }
}
