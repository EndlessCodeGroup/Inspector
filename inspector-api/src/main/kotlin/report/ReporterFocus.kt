package ru.endlesscode.inspector.report

/**
 * Interface that helps reporter focus on needed theme.
 */
public interface ReporterFocus {

    public companion object {
        @JvmStatic
        public val NO_FOCUS: ReporterFocus = object : ReporterFocus {
            override val focusedPackage: String = ""
            override val environment: ReportEnvironment = ReportEnvironment.EMPTY
        }
    }

    public val focusedPackage: String
    public val environment: ReportEnvironment
}
