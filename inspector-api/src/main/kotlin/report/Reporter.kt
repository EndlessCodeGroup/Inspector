package ru.endlesscode.inspector.report

public interface Reporter {

    public val focus: ReporterFocus

    /**
     * Enable or disable reporter.
     *
     * @see CachingReporter.report
     */
    public var enabled: Boolean

    /**
     * Alias for [addHandler] method.
     */
    public fun addHandler(
        beforeReport: (String, ExceptionData) -> Unit = { _, _ -> },
        onSuccess: (String, ExceptionData) -> Unit = { _, _ -> },
        onError: (Throwable) -> Unit = { throw it },
    ) {
        addHandler(object : ReportHandler {
            override fun beforeReport(message: String, exceptionData: ExceptionData) {
                beforeReport.invoke(message, exceptionData)
            }

            override fun onSuccess(message: String, exceptionData: ExceptionData) {
                onSuccess.invoke(message, exceptionData)
            }

            override fun onError(throwable: Throwable) {
                onError.invoke(throwable)
            }
        })
    }

    /**
     * Add given [handler] to the reporter.
     *
     * @see ReportHandler
     */
    public fun addHandler(handler: ReportHandler)

    /**
     * Report about [exception] with the [message] that describes when exception thrown (asynchronously).
     */
    public fun report(message: String, exception: Exception) {
        report(message, exception, async = true)
    }

    /**
     * Report about [exception] with the [message] that describes when exception thrown.
     *
     * @param async Asynchronously or not
     */
    public fun report(message: String, exception: Exception, async: Boolean)

    /**
     * Catch and report all exceptions that will be occurred inside [block].
     *
     * @param message The message that will be used as title
     * @param block Block that should be executed
     */
    public fun track(message: String, block: () -> Unit) {
        try {
            block.invoke()
        } catch (e: Exception) {
            report(message, e)
        }
    }

    public abstract class Builder {

        protected var focus: ReporterFocus = ReporterFocus.NO_FOCUS

        /**
         * Returns default fields that should be sent.
         */
        protected val fields: Set<ReportField>
            get() {
                val fields = mutableSetOf<ReportField>()
                fields.addAll(fieldsToSend.mapNotNull { focus.environment.fields[it] })
                fields.addAll(customFields)

                return fields
            }

        private var fieldsToSend: MutableSet<String> = mutableSetOf()
        private var customFields: MutableSet<ReportField> = mutableSetOf()

        /**
         * Assign focus.
         * Also copies default fields names to [fieldsToSend].
         *
         * @param focus The focus
         */
        public fun focusOn(focus: ReporterFocus): Builder {
            this.focus = focus
            fieldsToSend.addAll(focus.environment.fields.keys)
            return this
        }

        /**
         * Set default fields by names to report.
         */
        public fun setFields(vararg fields: String): Builder {
            this.fieldsToSend = fields.toMutableSet()
            return this
        }

        /**
         * Add default fields by names to report.
         */
        public fun addFields(vararg fields: String): Builder {
            this.fieldsToSend.addAll(fields)
            return this
        }

        /**
         * Remove default fields by names from report.
         */
        public fun removeFields(vararg fields: String): Builder {
            this.fieldsToSend.removeAll(fields)
            return this
        }

        /**
         * Add custom fields to report.
         */
        public fun addCustomFields(vararg customFields: ReportField): Builder {
            this.customFields.addAll(customFields)
            return this
        }

        public abstract fun build(): Reporter
    }
}
