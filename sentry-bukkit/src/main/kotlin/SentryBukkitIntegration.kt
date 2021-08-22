package ru.endlesscode.inspector.sentry.bukkit

import io.sentry.*
import org.bukkit.Server
import org.bukkit.plugin.Plugin

/**
 * [Integration] that handles Bukkit-specific info, like logging
 * server and plugin information.
 */
public class SentryBukkitIntegration(private val plugin: Plugin) : Integration {

    public companion object {

        private val KNOWN_SERVERS = listOf("Paper", "Spigot", "CraftBukkit", "BungeeCord", "WaterFall")
    }

    private val Server.knownName: String
        get() = (KNOWN_SERVERS.find { it in version } ?: "Unknown").toLowerCase()
    private val Server.minecraftVersion: String
        get() = bukkitVersion.substringBefore('-')

    override fun register(hub: IHub, options: SentryOptions) {
        options.sdkVersion?.addIntegration("bukkit")
        options.serverName = plugin.server.knownName
        options.setTag("minecraft", plugin.server.minecraftVersion)
        options.addEventProcessor(object : EventProcessor {
            override fun process(event: SentryEvent, hint: Any?): SentryEvent {
                return event.also { it.contexts.putAll(getContexts()) }
            }
        })
    }

    private fun getContexts(): Map<String, Map<String, Any>> {
        val serverMap = mapOf(
            "Name" to plugin.server.knownName,
            "Version" to plugin.server.version,
            "Minecraft Version" to plugin.server.minecraftVersion
        )
        val pluginMap = mapOf(
            "Name" to plugin.description.name,
            "Version" to plugin.description.version,
            "API Version" to plugin.description.apiVersion.toString()
        )
        return mapOf("server" to serverMap, "plugin" to pluginMap)
    }
}
