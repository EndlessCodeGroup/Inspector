plugins {
    publish
}

description = "Report problems to Sentry"

dependencies {
    implementation(sentry) {
        exclude(group = "com.google.code.gson") // GSON is bundled to Spigot
    }
}
