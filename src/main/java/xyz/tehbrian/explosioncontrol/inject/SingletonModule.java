package xyz.tehbrian.explosioncontrol.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.explosioncontrol.config.ConfigConfig;

public final class SingletonModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(ConfigConfig.class).asEagerSingleton();
    }

}
