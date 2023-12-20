package io.github.itzispyder.combatutils.modules.modules.combat;

import io.github.itzispyder.combatutils.modules.Category;
import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.combatutils.profiles.Profile;
import io.github.itzispyder.pdk.utils.misc.SoundPlayer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.joml.Math;

public class Criticals extends Module {

    public Criticals() {
        super("criticals", Category.PVP, "Land more damage hits.");
    }

    public static void handleAttack(EntityDamageByEntityEvent e) {
        Entity victim = e.getEntity();
        Entity attacker = e.getDamager();

        if (attacker instanceof Player player && victim instanceof LivingEntity living && attacker.isOnGround()) {
            Profile.get(player).ifEnabled(Criticals.class, m -> {
                e.setDamage(Math.min(e.getDamage() * 1.5, living.getHealth()));

                Location loc = living.getLocation();
                loc.getWorld().spawnParticle(Particle.CRIT, loc.add(0, 1.2, 0), 25, 0, 0, 0, 0.55);

                SoundPlayer sp = new SoundPlayer(loc, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1, 1);
                sp.playWithin(5);
            });
        }
    }
}
