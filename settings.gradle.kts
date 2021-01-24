rootProject.name = "inspector"

include(
    ":inspector-api", ":inspector-bukkit",
    ":inspector-sentry-reporter", ":sentry-bukkit",
    ":inspector-discord-reporter"
)
