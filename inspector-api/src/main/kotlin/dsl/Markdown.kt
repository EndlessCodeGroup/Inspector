package ru.endlesscode.inspector.dsl

internal interface Element {
    fun render(builder: StringBuilder, indent: String = "")
}

internal class Line(private val text: String) : Element {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text  \n")
    }
}

@DslMarker
internal annotation class MarkdownMarker

@MarkdownMarker
public abstract class Group internal constructor(
    private val indent: String,
    private val firstLine: String?,
    private val lastLine: String? = firstLine,
) : Element {
    private val children = arrayListOf<Element>()

    protected fun <T : Group> initGroup(group: T, init: T.() -> Unit): T {
        group.init()
        children.add(group)
        return group
    }

    override fun render(builder: StringBuilder, indent: String) {
        firstLine?.let { builder.append("$it\n") }
        for (c in children) {
            c.render(builder, indent + this.indent)
        }
        lastLine?.let { builder.append("$it\n") }
    }

    public operator fun String?.unaryPlus() {
        children.add(Line(this ?: ""))
    }

    public operator fun List<String>?.unaryPlus() {
        this?.forEach { +it }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder)
        return builder.toString()
    }
}

public abstract class TextGroup internal constructor() : Group(indent = "", firstLine = null) {

    public fun b(text: String): String = "**$text**"

    public fun it(text: String): String = "*$text*"

    public fun hr(): String = "---"
}

public class Markdown internal constructor() : TextGroup() {
    public fun code(lang: String = "", init: Code.() -> Unit): Code = initGroup(Code(lang), init)
}

public class Code internal constructor(lang: String) : Group(indent = "", firstLine = "```$lang", lastLine = "```")

public fun markdown(init: Markdown.() -> Unit): Markdown {
    return Markdown().also(init)
}
