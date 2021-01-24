package ru.endlesscode.inspector.sentry.bukkit

import io.sentry.IHub
import io.sentry.Integration
import io.sentry.SentryOptions
import org.bukkit.Server
import org.bukkit.plugin.Plugin

/**
 * SentryClientFactory that handles Bukkit-specific construction, like logging
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
        options.setBeforeSend { event, _ ->
            event.contexts.putAll(getContexts())
            event
        }
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
