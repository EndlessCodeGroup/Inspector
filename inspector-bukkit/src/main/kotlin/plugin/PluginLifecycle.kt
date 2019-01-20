package ru.endlesscode.inspector.bukkit.plugin

import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.PluginBase
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginLoader
import ru.endlesscode.inspector.api.PublicApi
import ru.endlesscode.inspector.api.report.Reporter
import ru.endlesscode.inspector.bukkit.command.TrackedCommandExecutor
import java.io.File
import java.io.InputStream
import java.io.Reader
import java.util.logging.Logger

abstract class PluginLifecycle : PluginBase() {

    @PublicApi
    val reporter: Reporter
        get() = holder.reporter

    lateinit var holder: TrackedPlugin

    private val trackedServer by lazy { TrackedServer(holder) }
    private val trackedPluginLoader by lazy { TrackedPluginLoader(holder.pluginLoader) }

    /**
     * Override this method if you want to do something on plugin's object initialization.
     */
    open fun init() {
        // To be overridden
    }

    final override fun getDataFolder(): File {
        return holder.dataFolder
    }

    final override fun getPluginLoader(): PluginLoader {
        return trackedPluginLoader
    }

    final override fun getServer(): Server {
        return trackedServer
    }

    final override fun isEnabled(): Boolean {
        return holder.isEnabled
    }

    final override fun getDescription(): PluginDescriptionFile {
        return holder.description
    }

    final override fun getConfig(): FileConfiguration {
        return holder.config
    }

    final override fun reloadConfig() {
        holder.reloadConfig()
    }

    final override fun saveConfig() {
        holder.saveConfig()
    }

    final override fun saveDefaultConfig() {
        holder.saveDefaultConfig()
    }

    final override fun saveResource(resourcePath: String?, replace: Boolean) {
        holder.saveResource(resourcePath, replace)
    }

    final override fun getResource(filename: String?): InputStream? {
        return holder.getResource(filename)
    }

    override fun onCommand(
            sender: CommandSender?,
            command: Command?,
            label: String?,
            args: Array<out String>?
    ): Boolean {
        return false
    }

    override fun onTabComplete(
            sender: CommandSender?,
            command: Command?,
            alias: String?,
            args: Array<out String>?
    ): MutableList<String>? {
        return null
    }

    override fun onLoad() {}

    override fun onEnable() {}

    override fun onDisable() {}

    override fun getDefaultWorldGenerator(worldName: String?, id: String?): ChunkGenerator? {
        return null
    }

    final override fun isNaggable(): Boolean {
        return holder.isNaggable
    }

    final override fun setNaggable(canNag: Boolean) {
        holder.isNaggable = canNag
    }

    final override fun getLogger(): Logger {
        return holder.logger
    }

    override fun toString(): String {
        return this.description.fullName
    }

    /**
     * Helper function to simplify CommandExecutor wrapping.
     */
    @PublicApi
    protected fun track(executor: CommandExecutor): CommandExecutor {
        return TrackedCommandExecutor(executor, reporter)
    }

    // For compatibility with JavaPlugin

    fun getCommand(name: String): PluginCommand? = holder.directGetCommand(name)

    @PublicApi
    protected fun getTextResource(file: String): Reader? = holder.directGetTextResource(file)

    @PublicApi
    protected fun getFile(): File = holder.directGetFile()

    @PublicApi
    protected fun getClassLoader(): ClassLoader = holder.directGetClassLoader()

    @PublicApi
    protected fun setEnabled(enabled: Boolean) {
        holder.directSetEnabled(enabled)
    }
}