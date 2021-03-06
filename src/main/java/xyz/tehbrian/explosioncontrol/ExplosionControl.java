package xyz.tehbrian.explosioncontrol;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.tehbrian.tehlib.core.configurate.Config;
import dev.tehbrian.tehlib.paper.TehPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.spongepowered.configurate.ConfigurateException;
import xyz.tehbrian.explosioncontrol.config.ConfigConfig;
import xyz.tehbrian.explosioncontrol.inject.SingletonModule;
import xyz.tehbrian.explosioncontrol.inject.PluginModule;

import java.util.List;

public final class ExplosionControl extends TehPlugin {

    private @MonotonicNonNull Injector injector;

    @Override
    public void onEnable() {
        try {
            this.injector = Guice.createInjector(
                    new PluginModule(this),
                    new SingletonModule()
            );
        } catch (final Exception e) {
            this.getSLF4JLogger().error("Something went wrong while creating the Guice injector.");
            this.getSLF4JLogger().error("Disabling plugin.");
            this.disableSelf();
            this.getSLF4JLogger().error("Printing stack trace, please send this to the developers:", e);
            return;
        }

        if (!this.loadConfiguration()) {
            this.disableSelf();
            return;
        }
        this.setupListeners();
    }

    /**
     * Loads the plugin's configuration. If an exception is caught, logs the
     * error and returns false.
     *
     * @return whether it was successful
     */
    public boolean loadConfiguration() {
        this.saveResourceSilently("config.yml");

        final List<Config> configsToLoad = List.of(
                this.injector.getInstance(ConfigConfig.class)
        );

        for (final Config config : configsToLoad) {
            try {
                config.load();
            } catch (final ConfigurateException e) {
                this.getSLF4JLogger().error("Exception caught during config load for {}", config.configurateWrapper().filePath());
                this.getSLF4JLogger().error("Please check your config.");
                this.getSLF4JLogger().error("Printing stack trace:", e);
                return false;
            }
        }

        this.getSLF4JLogger().info("Successfully loaded configuration.");
        return true;
    }

    private void setupListeners() {
        this.registerListeners(
                this.injector.getInstance(ExplosionListener.class)
        );
    }

}
