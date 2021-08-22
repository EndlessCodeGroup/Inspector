@file:Suppress("ClassName")

const val sentry = "io.sentry:sentry:5.1.1"

object fuel {
    private const val group = "com.github.kittinunf.fuel"
    private const val version = "2.3.1"
    const val self = "$group:fuel:$version"
    const val coroutines = "$group:fuel-coroutines:$version"
}
