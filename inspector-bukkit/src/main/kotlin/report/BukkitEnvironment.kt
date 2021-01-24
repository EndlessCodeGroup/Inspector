package ru.endlesscode.inspector.bukkit.report

import org.bukkit.plugin.Plugin
import ru.endlesscode.inspector.bukkit.Inspector
import ru.endlesscode.inspector.bukkit.util.printableForm
import ru.endlesscode.inspector.report.ReportEnvironment
import ru.endlesscode.inspector.report.ReportField
import ru.endlesscode.inspector.report.TextField

public class BukkitEnvironment internal constructor(
    plugin: Plugin,
    properties: Properties,
) : ReportEnvironment {

    public companion object {
        public const val FIELD_PLUGIN: String = "Plugin"
        public const val FIELD_CORE: String = "Server core"
        public const val FIELD_PLUGIN_LIST: String = "Installed plugins"
        public const val FIELD_INSPECTOR_VERSION: String = "Inspector version"
        public const val FIELD_REPORTER_ID: String = "Reporter ID"

        @JvmStatic
        internal val DEFAULT_PROPERTIES = Properties()
    }

    public val inspector: Inspector = Inspector(plugin, properties.configName)

    override val appName: String = plugin.description.name
    override val appVersion: String = plugin.description.version
    override val reporterId: String = inspector.serverId.toString()

    override val fields: Map<String, ReportField> = mapOf(
        FIELD_PLUGIN to TextField(FIELD_PLUGIN, plugin.printableForm),
        FIELD_CORE to TextField(
            FIELD_CORE,
            "${plugin.server.name} (${plugin.server.version})"
        ).showOnlyIf { inspector.shouldSendData(DataType.CORE) },

        FIELD_PLUGIN_LIST to PluginListField(
            plugin.server.pluginManager,
            properties.interestPluginsNames
        ).showOnlyIf { inspector.shouldSendData(DataType.PLUGINS) },

        FIELD_INSPECTOR_VERSION to TextField(FIELD_INSPECTOR_VERSION, Inspector.version),
        FIELD_REPORTER_ID to TextField(FIELD_REPORTER_ID, inspector.serverId.toString())
    )

    override val isInspectorEnabled: Boolean
        get() = inspector.isEnabled

    /**
     * Contains properties for environment customization.
     *
     * @param interestPluginsNames Names of the plugins that should be added to report. Empty list
     * means that we need to receive full list of plugins (this value used by default).
     * @param configName Name of config file that will be used for Inspector'sconfig. This file will
     * be stored in your plugin's folder. By default - "inspector.yml"
     */
    public class Properties @JvmOverloads constructor(
        public val interestPluginsNames: List<String> = emptyList(),
        public val configName: String = "inspector.yml",
    )
}
