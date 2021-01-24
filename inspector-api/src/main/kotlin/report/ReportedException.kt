package ru.endlesscode.inspector.report

public class ReportedException(cause: Throwable) : RuntimeException("Exception reported with Inspector", cause)
