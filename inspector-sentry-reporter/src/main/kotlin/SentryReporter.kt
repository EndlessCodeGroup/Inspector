package ru.endlesscode.inspector.report

import io.sentry.Integration
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.protocol.Message
import io.sentry.protocol.User

/**
 * Reporter that sends reports to Sentry.
 */
public class SentryReporter private constructor(
    dsn: String,
    integrations: List<Integration>,
    override val focus: ReporterFocus,
    private val fields: Set<ReportField>,
) : Reporter {

    override var enabled: Boolean = true

    private val handlers = CompoundReportHandler()

    init {
        Sentry.init { options ->
            options.dsn = dsn
            options.release = "${focus.environment.appName}@${focus.environment.appVersion}"
            options.addInAppInclude(focus.focusedPackage)
            options.enableUncaughtExceptionHandler = false
            integrations.forEach(options::addIntegration)
            options.addEventProcessor { event, _ ->
                fields.asSequence()
                    .filter(ReportField::show)
                    .forEach { event.setExtra(it.name, it.value) }
                event
            }
        }

        Sentry.configureScope { scope ->
            scope.user = User().apply {
                id = focus.environment.reporterId
            }
        }
    }

    override fun addHandler(handler: ReportHandler) {
        handlers.addHandler(handler)
    }

    override fun report(message: String, exception: Exception, async: Boolean) {
        val exceptionData = ExceptionData(exception)
        handlers.beforeReport(message, exceptionData)

        val event = SentryEvent(exception).apply {
            setMessage(Message().apply {
                formatted = message
            })
        }
        Sentry.captureEvent(event)

        // We can't track errors on error sending. So we just assume that report was sent successfully.
        handlers.onSuccess(message, exceptionData)
    }

    /**
     * Builder that should be used to build [SentryReporter].
     *
     * You should specify DSN with one of [setDataSourceName] methods.
     */
    public class Builder : Reporter.Builder() {

        private var dsn: String = ""
        private val integrations = mutableListOf<Integration>()

        /**
         * Set Sentry [dsn].
         * See: [Setting the DSN](https://docs.sentry.io/clients/java/config/#setting-the-dsn)
         */
        public fun setDsn(dsn: String): Builder {
            this.dsn = dsn
            return this
        }

        @Deprecated("Use setDsn", ReplaceWith("setDsn(dsn)"))
        public fun setDataSourceName(dsn: String): Builder {
            return setDsn(dsn)
        }

        @JvmOverloads
        @Deprecated("Use setDsn")
        public fun setDataSourceName(
            publicKey: String,
            projectId: String,
            protocol: String = "https",
            host: String = "sentry.io",
            port: String = "",
        ): Builder {
            val dsn = buildString {
                append(protocol)
                append("://")
                append(publicKey)
                append('@')
                append(host)
                if (port.isNotBlank()) append(':').append(port)
                append('/')
                append(projectId)
            }

            return setDsn(dsn)
        }

        /** Adds given [integration] to Sentry. */
        public fun addIntegration(integration: Integration): Builder {
            this.integrations.add(integration)
            return this
        }

        /**
         * Build configured [SentryReporter].
         */
        override fun build(): Reporter {
            require(dsn.isNotBlank()) {
                "You should specify DSN with method `setDsn(...)` and it shouldn't be blank."
            }

            return SentryReporter(
                dsn = dsn,
                integrations = integrations,
                focus = focus,
                fields = fields
            )
        }
    }
}
