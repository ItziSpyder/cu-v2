package io.github.itzispyder.combatutils.modules.modules.world;

import io.github.itzispyder.combatutils.modules.Category;
import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.pdk.Global;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

public class Flight extends Module {

    public Flight() {
        super("flight", Category.WORLD, "Enables flight for the player.");
    }

    @Override
    protected void onEnable() {
        getOwningPlayer().accept(player -> {
            player.setAllowFlight(true);
            player.setVelocity(new Vector(0, 1, 0));
            Bukkit.getScheduler().runTaskLater(Global.instance.getPlugin(), () -> player.setFlying(true), 1);
        });
    }

    @Override
    protected void onDisable() {
        getOwningPlayer().accept(player -> {
            player.setFlying(false);
            player.setAllowFlight(false);
        });
    }
}
