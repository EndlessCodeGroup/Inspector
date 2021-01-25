## [Unreleased]

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

[unreleased]: https://github.com/EndlessCodeGroup/Inspector/compare/v0.10.0...develop

[0.10.0]: https://github.com/EndlessCodeGroup/Inspector/compare/v0.9...v0.10.0
