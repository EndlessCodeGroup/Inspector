package ru.endlesscode.inspector.util

public fun json(vararg pairs: Pair<String, String>): String {
    return pairs.joinToString(prefix = "{", separator = ",", postfix = "}") { (key, value) ->
        """"$key":"${escape(value)}""""
    }
}

public fun escape(value: String): String {
    return value
        .replace("\\", "\\\\")
        .replace("\n", "\\n")
        .replace("\t", "\\t")
        .replace("\"", "\\\"")
}
