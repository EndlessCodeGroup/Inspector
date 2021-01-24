package ru.endlesscode.inspector.report

/**
 * Report handler. Used for tracking report process.
 */
public interface ReportHandler {

    /**
     * Called before report sent.
     *
     * @param message Report message
     * @param exceptionData Exception data that will be reported
     */
    public fun beforeReport(message: String, exceptionData: ExceptionData)

    /**
     * Called on successfully report.
     *
     * @param message Report message
     * @param exceptionData Exception data that was reported
     */
    public fun onSuccess(message: String, exceptionData: ExceptionData)

    /**
     * Called when an exception occurs while attempting to report an error.
     *
     * @param throwable The exception that occurred on report
     */
    public fun onError(throwable: Throwable)
}
