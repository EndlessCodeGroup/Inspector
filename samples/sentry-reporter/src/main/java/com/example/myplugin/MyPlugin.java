package com.example.myplugin;

import ru.endlesscode.inspector.bukkit.plugin.TrackedPlugin;
import ru.endlesscode.inspector.report.Reporter;
import ru.endlesscode.inspector.report.SentryReporter;
import ru.endlesscode.inspector.sentry.bukkit.SentryBukkitIntegration;

public class MyPlugin extends TrackedPlugin {

    public MyPlugin() {
        super(MyPluginLifecycle.class);
    }

    @Override
    protected Reporter createReporter() {
        String dsn = "https://845550f6ac0946c9bae87217906aa8e5@o209384.ingest.sentry.io/1331962";

        return new SentryReporter.Builder()
                .setDsn(dsn)
                .addIntegration(new SentryBukkitIntegration(this))
                .focusOn(this)
                .build();
    }

}
