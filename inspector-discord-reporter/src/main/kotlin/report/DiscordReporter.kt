package ru.endlesscode.inspector.report

import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.failure
import ru.endlesscode.inspector.dsl.markdown
import ru.endlesscode.inspector.service.HastebinStorage
import ru.endlesscode.inspector.service.TextStorage
import ru.endlesscode.inspector.util.getFocusedRootStackTrace
import ru.endlesscode.inspector.util.json
import ru.endlesscode.inspector.util.stackTraceText


/**
 * Reporter that uses Discord Webhook to write reports.
 */
public class DiscordReporter private constructor(
    override val focus: ReporterFocus,
    id: String,
    token: String,
    private val textStorage: TextStorage,
    private val username: String,
    private val avatarUrl: String,
    private val fields: Set<ReportField>,
) : CachingReporter() {

    public companion object {
        private const val DEFAULT_AVATAR_URL =
            "https://gitlab.com/endlesscodegroup/inspector/raw/master/images/inspector_icon_256.png"

        private val defaultTextStorage = HastebinStorage()
    }

    private val url = "https://discordapp.com/api/webhooks/$id/$token"

    override suspend fun report(
        title: String,
        exceptionData: ExceptionData,
        onSuccess: (String, ExceptionData) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        val exception = exceptionData.exception
        try {
            val fullReport = buildFullMessage(title, fields, exception)
            val fullReportUrl = textStorage.storeText(fullReport)
            val message = buildShortMessage(
                title = title,
                fields = fields,
                shortStackTrace = exception.getFocusedRootStackTrace(focus.focusedPackage),
                fullReportUrl = fullReportUrl
            )

            sendMessage(message, onError)
            onSuccess(title, exceptionData)
        } catch (e: Exception) {
            onError(e)
        }
    }

    private fun buildFullMessage(
        title: String,
        fields: Set<ReportField>,
        exception: Exception
    ): String {
        return buildString {
            append(title)
            append("\n\n")
            for (field in fields) {
                append(field.render(short = false))
                append("\n")
            }
            append("\nStacktrace:\n")
            append(exception.stackTraceText)
            append("\n")
        }
    }

    private fun buildShortMessage(
        title: String,
        fields: Set<ReportField>,
        shortStackTrace: String,
        fullReportUrl: String
    ): String {
        return markdown {
            val fieldsValues = fields.map { field ->
                field.render(prepareName = { b("$it:") }, separator = " ")
            }

            +b(title)
            +""
            +fieldsValues
            +b("Short stacktrace:")
            code("java") {
                +shortStackTrace
            }
            +"${b("Full report:")} $fullReportUrl"
            +hr()
        }.toString()
    }

    private fun sendMessage(content: String, onError: (Throwable) -> Unit) {
        val body = json(
            "username" to username,
            "avatar_url" to avatarUrl,
            "content" to content
        )

        url.httpPost()
            .header("user-agent" to "Inspector")
            .jsonBody(body)
            .response { request, response, result ->
                result.failure {
                    println(request)
                    println(response)
                    onError(it)
                }
            }
    }

    /**
     * Builder that should be used to build [DiscordReporter].
     *
     * You should specify Webhook data with method [hook].
     * Also here you can configure reporter username ([setUsername]) and avatar ([setAvatar]).
     */
    public class Builder : Reporter.Builder() {

        private val textStorage: TextStorage = defaultTextStorage
        private var id: String = ""
        private var token: String = ""
        private var username: String = "Inspector"
        private var avatarUrl: String = DEFAULT_AVATAR_URL

        /**
         * Build configured [DiscordReporter].
         */
        override fun build(): Reporter {
            require(id.isNotBlank() && token.isNotBlank()) {
                "You should specify Discord id and token with method `hook(...)` and it shouldn't be blank."
            }

            return DiscordReporter(
                focus = focus,
                id = id,
                textStorage = textStorage,
                token = token,
                username = username,
                avatarUrl = avatarUrl,
                fields = fields
            )
        }

        /**
         * Assign Discord Webhook data.
         *
         * @param id Webhook id (it contains only digits).
         * @param token Token for webhook (contains digits and small latin letters).
         */
        public fun hook(id: String, token: String): Builder {
            this.id = id
            this.token = token
            return this
        }

        /**
         * Set [username] that will be used as reporter username in Discord.
         */
        public fun setUsername(username: String): Builder {
            this.username = username
            return this
        }

        /**
         * Set [avatarUrl] that will be used as reporter avatar in Discord. Starting with protocol and including all slashes.
         */
        public fun setAvatar(avatarUrl: String): Builder {
            this.avatarUrl = avatarUrl
            return this
        }
    }
}
