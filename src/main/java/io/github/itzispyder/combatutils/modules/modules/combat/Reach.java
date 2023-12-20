package io.github.itzispyder.combatutils.modules.modules.combat;

import io.github.itzispyder.combatutils.modules.Category;
import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.combatutils.profiles.Profile;
import io.github.itzispyder.pdk.utils.raytracers.CustomDisplayRaytracer;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class Reach extends Module {

    public Reach() {
        super("reach", Category.PVP, "Gives you really long arms.");
    }

    public static void handleInteraction(PlayerInteractEvent e) {
        if (!e.getAction().isLeftClick()) {
            return;
        }
        Profile.get(e.getPlayer()).ifEnabled(Reach.class, m -> m.reach(6));
    }

    public void reach(double dist) {
        getOwningPlayer().accept(attacker -> attack(attacker, dist, 0.2));
    }

    public static void attack(Player attacker, double dist, double expand) {
        Location eye = attacker.getEyeLocation();
        Vector dir = attacker.getLocation().getDirection();
        var query = CustomDisplayRaytracer.hitAnythingExclude(attacker);
        var hit = CustomDisplayRaytracer.trace(eye, dir, dist, 0.3, query);

        var targets = hit.getNearbyEntities(attacker, 5, true, expand, ent -> {
            return ent instanceof LivingEntity && !ent.isDead();
        });

        if (!targets.isEmpty()) {
            attacker.attack(targets.get(0));
        }
    }
}
