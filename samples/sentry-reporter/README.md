# Sentry Reporter
Reporter that sends reports to [Sentry](https://sentry.io/).  

### Gradle
```groovy
ext.inspectorVersion = "0.12.0"
dependencies {
    implementation "ru.endlesscode.inspector:inspector-sentry-reporter:$inspectorVersion"
    implementation "ru.endlesscode.inspector:sentry-bukkit:$inspectorVersion" // If you want SentryBukkitIntegration
}
```

## Usage
You should return `SentryReporter` in `createReporter` method:
```java
@Override
protected Reporter createReporter(){
        String dsn="[YOUR_DSN_HERE]";

        return new SentryReporter.Builder()
        .setDsn(dsn)
        // If you want more detailed reports, add this, but you also should
        // add `sentry-bukkit` dependency before
        .addIntegration(new SentryBukkitIntegration(this))
        .focusOn(this) // this - instance of TrackedPlugin
            .build();
}
```
Setting DSN is required.
There are also available `setDataSourceName(String dsn)` if you want just use DSN link instead.
