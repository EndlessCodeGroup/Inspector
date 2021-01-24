import ru.endlesscode.bukkitgradle.dependencies.spigot
import ru.endlesscode.bukkitgradle.dependencies.spigotApi

// Bukkit implementation build config

plugins {
    id("ru.endlesscode.bukkitgradle") version "0.9.1"
    `maven-publish`
}

bukkit {
    version = "1.16.4"

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

// TODO: Port it to Kotlin DSL
apply(from = "groovy.gradle")
