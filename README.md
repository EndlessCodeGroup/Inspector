# Inspector

![Inspector Example](https://gitlab.com/endlesscodegroup/inspector/raw/develop/images/example.png)  

Inspector helps developers to track all exceptions and crashes of theirs plugins.
It automatically sends reports to the developer with all needed information about the environment.

It sends:
- Plugin name and version
- Server core and version
- List of plugins with versions
- Exception stacktrace

## For server owners
Just copy plugin to `plugins/` folder.
Also you can disable sending of information about server core and installed plugins in the `inspector.yml` that stored in directory of the each plugin that uses Inspector.

### Config example
```yaml
Reporter:
  enabled: true 
  # Here you can choose what you don't want to send
  data:
    core: true    # Info about server core
    plugins: true # Plugins list
```

## For plugin developers

To add Inspector to the plugin you should:
- Add Inspector as a dependency to the project
- Add Inspector to plugin.yml to `depend` section
- Modify main plugin class
- Change main class in `plugin.yml` to new
- Change all usages of `BukkitRunnable` to `TrackedBukkitRunnable`
- Wrap `CommandExecutor` with `TrackedCommandExecutor` 

### Add Inspector to the project
```groovy
repositories { 
    maven { 
        url "https://dl.bintray.com/endlesscode/repo" 
    } 
}

dependencies {
    compileOnly "ru.endlesscode.inspector:inspector-bukkit:0.6.0"
}
```

### Main plugin class modifications

First of all your current main plugin class should extend `PluginLifecycle` instead of `JavaPlugin`.  
For example, this code:
```java
public class MyPlugin extends JavaPlugin {
    // ...
    // onEnable, onDisable, etc.
    // ...
}
```
should become:
```java
public class MyPlugin extends PluginLifecycle {
    // ...
    // onEnable, onDisable, etc.
    // ...
}
```

And then you should create the new class extending `TrackedPlugin` that will be used as main and linked with the lifecycle.
Also you should override method `createReporter()`. Created reporter will be used for reporting errors.  
Example:
```java
public class MyTrackedPlugin extends TrackedPlugin {
    
    public MyTrackedPlugin() {
        super(MyPlugin.class); // Pass here lifecycle class
    }
    
    @Override
    public Reporter createReporter() {
        return new DiscordReporter.Builder()
                .hook("<DISCORD_WEBHOOK_ID>", "<DISCORD_WEBHOOK_TOKEN>")
                .focusOn(this) // Reporter will be focused on this plugin
                .build();
        // For more reporter customization see DiscordReporter.Builder and Reporter.Builder classes
    }
}
```
You can see example of plugin migration here: endlesscodegroup/rpginventory/rpginventory@33bca425

> **NOTE:** At this moment available only Discord Reporter, but will be added more reporters soon.

### Bundle Inspector

*Docs will be added soon...*
