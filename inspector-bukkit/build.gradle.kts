import ru.endlesscode.bukkitgradle.dependencies.spigot
import ru.endlesscode.bukkitgradle.dependencies.spigotApi

// Bukkit implementation build config

plugins {
    id("ru.endlesscode.bukkitgradle") version "0.10.0"
    publish
}

description = "Inspector implementation for Bukkit"

bukkit {
    apiVersion = "1.17.1"

    meta {
        name.set("Inspector")
        main.set("$group.${project.name}.${name}Plugin")
        authors.set(listOf("OsipXD", "EndlessCodeGroup"))
        url.set("https://gitlab.com/endlesscodegroup/inspector")
    }

    server {
        setCore("paper")
        eula = true
    }
}

repositories {
    spigot()
}

dependencies {
    compileOnly(spigotApi) { isTransitive = false }
    implementation("org.slf4j:slf4j-jdk14:1.7.30")
}
