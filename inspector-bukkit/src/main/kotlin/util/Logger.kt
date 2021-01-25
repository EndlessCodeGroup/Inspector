package ru.endlesscode.inspector.bukkit.util

import java.util.logging.Level
import java.util.logging.Logger

private const val DEBUG_TAG = "[DEBUG]"

internal fun Logger.debug(message: String, throwable: Throwable) {
    log(Level.INFO, "$DEBUG_TAG$message", throwable)
}

internal fun Logger.fixDebugLogs() {
    // Use INFO level and replace it with FINE after isLoggable check.
    setFilter { record ->
        if (record.message?.startsWith(DEBUG_TAG) == true) record.level = Level.FINE
        true
    }
}
