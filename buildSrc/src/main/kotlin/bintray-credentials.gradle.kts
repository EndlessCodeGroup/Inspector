if (!hasProperty("bintrayUsername") || !hasProperty("bintrayPassword")) {
    project.logger.log(LogLevel.WARN,
        """
        Bintray credentials not found. Uploading to bintray is unavailable.
        To make it possible, set 'bintrayUsername' and 'bintrayPassword'.
        """.trimIndent()
    )
}
