package xyz.tehbrian.explosioncontrol;

import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.tehbrian.explosioncontrol.config.ConfigConfig;

public class ExplosionListener implements Listener {

    private final ConfigConfig configConfig;

    @Inject
    public ExplosionListener(final @NonNull ConfigConfig configConfig) {
        this.configConfig = configConfig;
    }

    @EventHandler
    public void onEntityExplode(final EntityExplodeEvent event) {
        final ConfigConfig.@Nullable WorldData worldData = this.configConfig.worlds().get(event.getEntity().getWorld().getName());
        if (worldData == null) {
            return;
        }
        final ConfigConfig.WorldData.@Nullable EntityData entityData = worldData.entity().get(event.getEntity().getType());
        if (entityData == null) {
            return;
        }
        if (entityData.disableExplosion()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByEntity(final EntityDamageByEntityEvent event) {
        final ConfigConfig.@Nullable WorldData worldData = this.configConfig.worlds().get(event.getDamager().getWorld().getName());
        if (worldData == null) {
            return;
        }
        final ConfigConfig.WorldData.@Nullable EntityData entityData = worldData.entity().get(event.getDamager().getType());
        if (entityData == null) {
            return;
        }
        if (entityData.preventDamage().contains(event.getEntity().getType())) {
            event.setCancelled(true);
        }
    }

//    @EventHandler
//    public void onHangingBreakByEntity(final HangingBreakByEntityEvent event) {
// for some frikin reason this one is reporting remover type as player?? even when it's not?????
//        System.out.println("Hanging");
//        System.out.println(event.getEntity().getType());
//        if (event.getRemover() == null) {
//            return;
//        }
//        System.out.println(event.getRemover().getType());
//        final ConfigConfig.@Nullable WorldData worldData = this.configConfig.worlds().get(event.getRemover().getWorld().getName());
//        if (worldData == null) {
//            return;
//        }
//        final ConfigConfig.WorldData.@Nullable EntityData entityData = worldData.entity().get(event.getRemover().getType());
//        if (entityData == null) {
//            return;
//        }
//        if (entityData.preventDamage().contains(event.getEntity().getType())) {
//            event.setCancelled(true);
//        }
//    }
//
//    @EventHandler
//    public void onBlockExplode(final BlockExplodeEvent event) {
//        final ConfigConfig.@Nullable WorldData worldData = this.configConfig.worlds().get(event.getBlock().getWorld().getName());
//        if (worldData == null) {
//            return;
//        }
//        final ConfigConfig.WorldData.@Nullable BlockData blockData = worldData.block().get(event.getBlock().getType());
//        if (blockData == null) {
//            return;
//        }
//        if (blockData.disableExplosion()) {
//            event.setCancelled(true);
//        }
//    }
//
//    @EventHandler
//    public void onDamageByBlock(final EntityDamageByBlockEvent event) {
//        System.out.println("DamageByBlock");
//        System.out.println(event.getDamager());
//        System.out.println(event.getEntity().getType());
//        if (event.getDamager() == null) {
//            return;
//        }
//        final ConfigConfig.@Nullable WorldData worldData = this.configConfig.worlds().get(event.getDamager().getWorld().getName());
//        if (worldData == null) {
//            return;
//        }
//        final ConfigConfig.WorldData.@Nullable BlockData blockData = worldData.block().get(event.getDamager().getType());
//        if (blockData == null) {
//            return;
//        }
//        if (blockData.preventDamage().contains(event.getEntity().getType())) {
//            event.setCancelled(true);
//        }
//    }

}
