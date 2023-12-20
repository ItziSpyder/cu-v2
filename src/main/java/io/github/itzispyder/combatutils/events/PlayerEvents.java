package io.github.itzispyder.combatutils.events;

import io.github.itzispyder.combatutils.modules.modules.combat.AutoTotem;
import io.github.itzispyder.combatutils.modules.modules.combat.Criticals;
import io.github.itzispyder.combatutils.modules.modules.combat.Hitboxes;
import io.github.itzispyder.combatutils.modules.modules.combat.Reach;
import io.github.itzispyder.combatutils.modules.modules.world.AntiKb;
import io.github.itzispyder.combatutils.modules.modules.world.NoFall;
import io.github.itzispyder.pdk.events.CustomListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class PlayerEvents implements CustomListener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Reach.handleInteraction(e);
        Hitboxes.handleInteraction(e);
    }

    @EventHandler
    public void onDamageByAnother(EntityDamageByEntityEvent e) {
        Criticals.handleAttack(e);
    }

    @EventHandler
    public void onDamageByAnother(EntityDamageEvent e) {
        AutoTotem.handleDamage(e);
        NoFall.handleDamage(e);
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
        AntiKb.handleVelocity(e);
    }
}
