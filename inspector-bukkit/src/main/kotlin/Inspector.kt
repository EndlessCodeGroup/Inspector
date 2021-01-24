package ru.endlesscode.inspector.bukkit

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import ru.endlesscode.inspector.bukkit.report.DataType
import ru.endlesscode.inspector.bukkit.util.FileUtil
import ru.endlesscode.inspector.bukkit.util.getBooleanOrPut
import ru.endlesscode.inspector.bukkit.util.getUuidOrPut
import java.io.File
import java.util.*

/**
 * Class that represents Inspector's configurations.
 */
public class Inspector internal constructor(private val configFile: File, private val globalConfigFile: File) {

    public companion object {
        /**
         * Version of Inspector.
         */
        public const val version: String = "0.9"

        // Preserved value for case if global config not contains server ID yet
        private val newServerId by lazy { UUID.randomUUID() }
    }

    /** Enabling of Inspector. */
    public var isEnabled: Boolean = true
        private set

    /**
     * Unique ID of server. It can be used to determine what reports sent from the same server.
     */
    public var serverId: UUID
        private set

    private var sendData = mutableMapOf(
        DataType.CORE to true,
        DataType.PLUGINS to true
    )

    private val globalConfig = YamlConfiguration()
    private val config = YamlConfiguration()

    init {
        FileUtil.createFileIfNotExists(globalConfigFile)
        FileUtil.createFileIfNotExists(configFile)

        serverId = readServerId()

        reload()
    }

    internal constructor(
        plugin: Plugin,
        configName: String,
    ) : this(plugin.dataFolder.resolve(configName), plugin.dataFolder.parentFile.resolve("Inspector/config.yml"))

    /**
     * Reload config from the disk.
     */
    public fun reload() {
        globalConfig.load(globalConfigFile)
        config.load(configFile)

        readValuesFromConfig()

        globalConfig.save(globalConfigFile)
        config.save(configFile)
    }

    /**
     * Checks that sending of the data with specified [type][dataType], enabled in config.
     * @return `true` if sending is enabled, otherwise `false`.
     */
    public fun shouldSendData(dataType: DataType): Boolean = sendData.getValue(dataType)

    private fun readValuesFromConfig() {
        isEnabled = readBoolean("Reporter.enabled")
        sendData[DataType.CORE] = readBoolean("Reporter.data.core")
        sendData[DataType.PLUGINS] = readBoolean("Reporter.data.plugins")
        serverId = readServerId()
    }

    private fun readBoolean(path: String): Boolean {
        // Assumes that `false` more important than `true` and `true` is default value.
        return config.getBooleanOrPut(path, true) && globalConfig.getBooleanOrPut(path, true)
    }

    private fun readServerId(): UUID {
        return globalConfig.getUuidOrPut("Reporter.server") { newServerId }
    }
}
