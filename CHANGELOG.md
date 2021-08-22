## [Unreleased]

### Added

- Bukkit 1.17 support

### Housekeeping

- Gradle 6.8.3 -> 7.2

## [0.11.0] (2021-02-23)

### Fallback reporter

Now you can return `null` from `createReporter`.
In this case will be used `LoggerReporter` as a fallback.
This reporter simply prints everything to the plugin's logger, just like you not use Inspector.

### Changed

- Better interoperability with Java

### Housekeeping

- Migrate from JCenter to Maven Central
- Update Gradle to 6.8.3
- Update Sentry to 4.2.0

## [0.10.1]

### Fixed

- Make `PluginLifecycle.getReporter()` public to keep compatibility

## [0.10.0]

### Updated sentry integration

Sentry updated from 1.7.29 to 4.0.0-beta.1. It is impossible to do this upgrade without breaking changes, so way to set
up sentry reporter slightly changed.

```diff
public Reporter createReporter() {
    String dsn = "[YOUR_DSN_HERE]";

    return new SentryReporter.Builder()
-           .setDataSourceName(dsn)
+           .setDsn(dsn)
-           .setClientFactory(new BukkitPluginSentryClientFactory(this))
+           .addIntegration(new SentryBukkitIntegration(this))
            .focusOn(this) // Reporter will be focused on this plugin
            .build();
}
```

### Fixed

- Debug logging of all exceptions to log file

### Housekeeping

- Updated Kotlin to 1.4.21
- Updated Coroutines and Fuel
- Build infrastructure updated

[unreleased]: https://github.com/EndlessCodeGroup/Inspector/compare/v0.11.0...develop
[0.11.0]: https://github.com/EndlessCodeGroup/Inspector/compare/v0.10.1...v0.11.0
[0.10.1]: https://github.com/EndlessCodeGroup/Inspector/compare/v0.10.0...v0.10.1
[0.10.0]: https://github.com/EndlessCodeGroup/Inspector/compare/v0.9...v0.10.0
