package io.github.itzispyder.combatutils.modules.modules.world;

import io.github.itzispyder.combatutils.modules.Category;
import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.combatutils.profiles.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoFall extends Module {

    public NoFall() {
        super("no-fall", Category.WORLD, "No fall damage anymore.");
    }

    public static void handleDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }

        Profile.get(player).ifEnabled(NoFall.class, m -> {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
            }
        });
    }
}
