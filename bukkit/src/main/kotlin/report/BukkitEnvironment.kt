package ru.endlesscode.inspector.bukkit.report

import org.bukkit.plugin.Plugin
import ru.endlesscode.inspector.api.report.ReportEnvironment
import ru.endlesscode.inspector.api.report.ReportField
import ru.endlesscode.inspector.api.report.TextField
import ru.endlesscode.inspector.bukkit.Inspector
import ru.endlesscode.inspector.bukkit.util.printableForm


class BukkitEnvironment(
    plugin: Plugin,
    properties: BukkitEnvironment.Properties
) : ReportEnvironment {

    companion object {
        const val TAG_PLUGIN = "Plugin"
        const val TAG_CORE = "Server core"
        const val TAG_PLUGIN_LIST = "Installed plugins"
        const val TAG_INSPECTOR_VERSION = "Inspector version"
        const val TAG_REPORTER_ID = "Reporter ID"

        @JvmStatic
        internal val DEFAULT_PROPERTIES = Properties()
    }

    public val inspector = Inspector(plugin, properties.configName)

    override val fields: Map<String, ReportField>

    override val defaultFieldsTags: List<String> = listOf(
        TAG_PLUGIN, TAG_CORE, TAG_PLUGIN_LIST, TAG_INSPECTOR_VERSION, TAG_REPORTER_ID
    )

    override val isInspectorEnabled: Boolean
        get() = inspector.isEnabled

    init {
        fields = mapOf(
            TAG_PLUGIN to TextField(TAG_PLUGIN, plugin.printableForm),
            TAG_CORE to TextField(
                TAG_CORE,
                "${plugin.server.name} (${plugin.server.version})"
            ) { inspector.shouldSendData(DataType.CORE) },

            TAG_PLUGIN_LIST to PluginListField(
                plugin.server.pluginManager.plugins.asList(),
                properties.interestPluginsNames
            ) { inspector.shouldSendData(DataType.PLUGINS) },

            TAG_INSPECTOR_VERSION to TextField(TAG_INSPECTOR_VERSION, Inspector.version),
            TAG_REPORTER_ID to TextField(TAG_REPORTER_ID, inspector.serverId.toString())
        )
    }

    /**
     * Contains properties for environment customization.
     *
     * @param interestPluginsNames Names of the plugins that should be added to report. Empty list
     * means that we need to receive full list of plugins (this value used by default).
     * @param configName Name of config file that will be used for Inspector'sconfig. This file will
     * be stored in your plugin's folder. By default - "inspector.yml"
     */
    class Properties @JvmOverloads constructor(
        val interestPluginsNames: List<String> = emptyList(),
        val configName: String = "inspector.yml"
    )
}