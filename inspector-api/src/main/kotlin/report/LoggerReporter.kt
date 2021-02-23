package ru.endlesscode.inspector.report

import java.util.logging.Level
import java.util.logging.Logger

public class LoggerReporter(
    private val logger: Logger,
    override val focus: ReporterFocus,
) : Reporter {

    override var enabled: Boolean = true

    private val handlers = CompoundReportHandler()

    override fun addHandler(handler: ReportHandler) {
        handlers.addHandler(handler)
    }

    override fun report(message: String, exception: Exception, async: Boolean) {
        val exceptionData = ExceptionData(exception)
        handlers.beforeReport(message, exceptionData)
        logger.log(Level.WARNING, message, exception)
        handlers.onSuccess(message, exceptionData)
    }
}
