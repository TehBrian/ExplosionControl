package xyz.tehbrian.explosioncontrol.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.explosioncontrol.ExplosionControl;

import java.nio.file.Path;

/**
 * Guice module which provides bindings for the plugin's instances.
 */
public final class PluginModule extends AbstractModule {

    private final ExplosionControl explosionControl;

    /**
     * @param explosionControl injected
     */
    public PluginModule(final @NonNull ExplosionControl explosionControl) {
        this.explosionControl = explosionControl;
    }

    @Override
    protected void configure() {
        this.bind(ExplosionControl.class).toInstance(this.explosionControl);
        this.bind(JavaPlugin.class).toInstance(this.explosionControl);
    }

    /**
     * Provides the plugin's Log4J logger.
     *
     * @return the plugin's Log4J logger
     */
    @Provides
    public @NonNull Logger provideLog4JLogger() {
        return this.explosionControl.getLog4JLogger();
    }

    /**
     * Provides the plugin's data folder.
     *
     * @return the data folder
     */
    @Provides
    @Named("dataFolder")
    public @NonNull Path provideDataFolder() {
        return this.explosionControl.getDataFolder().toPath();
    }

}
