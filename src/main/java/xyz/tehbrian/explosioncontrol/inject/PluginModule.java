package xyz.tehbrian.explosioncontrol.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.explosioncontrol.ExplosionControl;

import java.nio.file.Path;

public final class PluginModule extends AbstractModule {

    private final ExplosionControl explosionControl;

    public PluginModule(final @NonNull ExplosionControl explosionControl) {
        this.explosionControl = explosionControl;
    }

    @Override
    protected void configure() {
        this.bind(ExplosionControl.class).toInstance(this.explosionControl);
        this.bind(JavaPlugin.class).toInstance(this.explosionControl);
    }

    /**
     * @return the plugin's SLF4J logger
     */
    @Provides
    public @NonNull Logger provideSLF4JLogger() {
        return this.explosionControl.getSLF4JLogger();
    }

    /**
     * @return the plugin's data folder
     */
    @Provides
    @Named("dataFolder")
    public @NonNull Path provideDataFolder() {
        return this.explosionControl.getDataFolder().toPath();
    }

}
