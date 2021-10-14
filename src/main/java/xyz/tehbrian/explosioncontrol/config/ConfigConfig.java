package xyz.tehbrian.explosioncontrol.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.tehlib.core.configurate.AbstractConfig;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Loads and holds values for {@code config.yml}.
 */
public final class ConfigConfig extends AbstractConfig<YamlConfigurateWrapper> {

    private final Map<String, WorldData> worlds = new HashMap<>();

    /**
     * @param dataFolder the data folder
     */
    @Inject
    public ConfigConfig(final @NonNull @Named("dataFolder") Path dataFolder) {
        super(new YamlConfigurateWrapper(dataFolder.resolve("config.yml")));
    }

    // what the fuck is this
    @Override
    public void load() throws ConfigurateException {
        this.configurateWrapper.load();
        final @NonNull CommentedConfigurationNode rootNode = Objects.requireNonNull(this.configurateWrapper.get()); // will not be null as we called #load()
        final @NonNull CommentedConfigurationNode worldsNode = Objects.requireNonNull(rootNode.node("worlds"));

        worlds.clear();

        for (final Map.Entry<Object, CommentedConfigurationNode> worldNodes : worldsNode.childrenMap().entrySet()) {
            final Map<EntityType, WorldData.EntityData> entity = new HashMap<>();
            final @Nullable CommentedConfigurationNode entityNode = worldNodes.getValue().node("entity");
            if (entityNode != null) {
                for (final Map.Entry<Object, CommentedConfigurationNode> entityStuff : entityNode.childrenMap().entrySet()) {
                    final EntityType entityType;
                    try {
                        entityType = EntityType.valueOf(entityStuff.getKey().toString().toUpperCase(Locale.ROOT));
                    } catch (final IllegalArgumentException e) {
                        e.printStackTrace();
                        continue;
                    }

                    final List<EntityType> preventDamage = new ArrayList<>();
                    for (final String name : Objects.requireNonNull(entityStuff.getValue().node("prevent-damage").getList(String.class))) {
                        try {
                            preventDamage.add(EntityType.valueOf(name.toUpperCase(Locale.ROOT)));
                        } catch (final IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }

                    entity.put(entityType, new WorldData.EntityData(
                            entityStuff.getValue().node("disable-explosion").getBoolean(false),
                            preventDamage
                    ));
                }
            }

            final Map<Material, WorldData.BlockData> block = new HashMap<>();
            final @Nullable CommentedConfigurationNode blockNode = worldNodes.getValue().node("block");
            if (blockNode != null) {
                for (final Map.Entry<Object, CommentedConfigurationNode> blockStuff : blockNode.childrenMap().entrySet()) {
                    final Material material;
                    try {
                        material = Material.valueOf(blockStuff.getKey().toString().toUpperCase(Locale.ROOT));
                    } catch (final IllegalArgumentException e) {
                        e.printStackTrace();
                        continue;
                    }

                    final List<EntityType> preventDamage = new ArrayList<>();
                    for (final String name : Objects.requireNonNull(blockStuff.getValue().node("prevent-damage").getList(String.class))) {
                        try {
                            preventDamage.add(EntityType.valueOf(name.toUpperCase(Locale.ROOT)));
                        } catch (final IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }

                    block.put(material, new WorldData.BlockData(
                            blockStuff.getValue().node("disable-explosion").getBoolean(false),
                            preventDamage
                    ));
                }
            }

            this.worlds.put(worldNodes.getKey().toString(), new WorldData(entity, block));
        }
    }

    public @NonNull Map<@NonNull String, @NonNull WorldData> worlds() {
        return worlds;
    }

    public record WorldData(Map<EntityType, EntityData> entity,
                            Map<Material, BlockData> block) {

        public record EntityData(boolean disableExplosion,
                                 List<EntityType> preventDamage) {

        }

        public record BlockData(boolean disableExplosion,
                                List<EntityType> preventDamage) {

        }

    }

}
