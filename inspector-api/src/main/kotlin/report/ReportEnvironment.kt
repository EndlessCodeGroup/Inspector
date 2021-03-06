package ru.endlesscode.inspector.report

public interface ReportEnvironment {

    public companion object {
        @JvmStatic
        public val EMPTY: ReportEnvironment = object : ReportEnvironment {
            override val appName: String = ""
            override val appVersion: String = ""
            override val reporterId: String = ""
            override val fields: Map<String, ReportField> = emptyMap()
            override val isInspectorEnabled: Boolean = false
        }
    }

    /**
     * Name of app that uses Inspector.
     */
    public val appName: String

    /**
     * Version of app that uses Inspector.
     */
    public val appVersion: String

    /**
     * Unique identifier of reporter.
     */
    public val reporterId: String

    /**
     * Environment-related [fields][ReportField]. Stored as relation "name -> field".
     */
    public val fields: Map<String, ReportField>

    /**
     * Indicates that inspector enabled.
     */
    public val isInspectorEnabled: Boolean
}
