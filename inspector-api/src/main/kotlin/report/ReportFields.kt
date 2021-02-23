package ru.endlesscode.inspector.report

public interface ReportField {

    public companion object {
        private const val HIDDEN_FIELD_VALUE = "<value hidden by user>"
    }

    public val name: String
    public val shortValue: String
    public val value: String
    public val show: Boolean

    @JvmDefault
    public fun render(
        short: Boolean = true,
        separator: String = ": ",
        prepareName: (String) -> String = { it },
        prepareValue: (String) -> String = { it },
    ): String {
        val selectedValue = if (show) {
            if (short) shortValue else value
        } else {
            HIDDEN_FIELD_VALUE
        }

        return "${prepareName(name)}$separator${prepareValue(selectedValue)}"
    }
}

public open class TextField(
    override val name: String,
    override val shortValue: String,
    override val value: String = shortValue,
) : ReportField {

    private var shouldShow: TextField.() -> Boolean = { true }

    final override val show: Boolean
        get() = shouldShow()

    /** Adds predicate to show or hide field. */
    public fun showOnlyIf(predicate: TextField.() -> Boolean): ReportField {
        shouldShow = predicate
        return this
    }
}

public open class ListField<T>(
    override val name: String,
    private val produceList: () -> List<T>,
    private val getSummary: (List<T>) -> String,
) : ReportField {

    override val shortValue: String
        get() = getSummary(list)

    override val value: String
        get() = list.joinToString("\n", prefix = "\n") { "- $it" }

    final override val show: Boolean
        get() = shouldShow()

    protected open val list: List<T>
        get() = produceList()

    private var shouldShow: ListField<T>.() -> Boolean = { true }

    /** Adds predicate to show or hide field. */
    public fun showOnlyIf(predicate: ListField<T>.() -> Boolean): ListField<T> {
        shouldShow = predicate
        return this
    }
}
