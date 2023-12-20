package io.github.itzispyder.combatutils.modules.modules.combat;

import io.github.itzispyder.combatutils.modules.Category;
import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.combatutils.profiles.Profile;
import org.bukkit.event.player.PlayerInteractEvent;

public class Hitboxes extends Module {

    public Hitboxes() {
        super("hitboxes", Category.PVP, "Extends enemy hitboxes.");
    }

    public static void handleInteraction(PlayerInteractEvent e) {
        if (!e.getAction().isLeftClick()) {
            return;
        }
        Profile.get(e.getPlayer()).ifEnabled(Hitboxes.class, m -> m.hitbox(6));
    }

    public void hitbox(double dist) {
        getOwningPlayer().accept(attacker -> Reach.attack(attacker, dist, 5));
    }
}
