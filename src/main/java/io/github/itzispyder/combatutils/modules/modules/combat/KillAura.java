package io.github.itzispyder.combatutils.modules.modules.combat;

import io.github.itzispyder.combatutils.modules.Category;
import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.pdk.utils.raytracers.CustomDisplayRaytracer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class KillAura extends Module {

    public KillAura() {
        super("kill-aura", Category.PVP, "An aura of death around you.");
    }

    @Override
    public void onTick() {
        getOwningPlayer().accept(player -> {
            if (player.getAttackCooldown() < 0.9) {
                return;
            }

            var loc = CustomDisplayRaytracer.blocksInFrontOf(player.getLocation(), player.getLocation().getDirection(), 0, true);
            var targets = loc.getNearbyEntities(player, 6, false, ent -> {
                return ent instanceof LivingEntity && !ent.isDead();
            });
            int i = 0;

            for (Entity target : targets) {
                if (i++ >= 5) {
                    break;
                }
                player.attack(target);
            }
        });
    }
}
