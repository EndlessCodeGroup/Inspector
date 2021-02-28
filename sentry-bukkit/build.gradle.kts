plugins {
    publish
}

description = "Sentry Bukkit integration"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT") { isTransitive = false }
    api(sentry)
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}
