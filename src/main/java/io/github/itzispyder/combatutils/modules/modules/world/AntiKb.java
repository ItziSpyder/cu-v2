package io.github.itzispyder.combatutils.modules.modules.world;

import io.github.itzispyder.combatutils.modules.Category;
import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.combatutils.profiles.Profile;
import org.bukkit.event.player.PlayerVelocityEvent;

public class AntiKb extends Module {

    public AntiKb() {
        super("anti-kb", Category.WORLD, "Prevents most knockback.");
    }

    public static void handleVelocity(PlayerVelocityEvent e) {
        Profile.get(e.getPlayer()).ifEnabled(AntiKb.class, m -> e.setCancelled(true));
    }
}
