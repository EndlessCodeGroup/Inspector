plugins {
    publish
}

description = "Sentry Bukkit integration"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT") { isTransitive = false }
    api(sentry) {
        exclude(group = "com.google.code.gson") // GSON is bundled to Spigot
    }
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}
