package io.github.itzispyder.combatutils.modules.modules.crystal;

import io.github.itzispyder.combatutils.CombatUtils;
import io.github.itzispyder.combatutils.modules.Category;
import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.combatutils.profiles.Profile;
import io.github.itzispyder.combatutils.util.Hotbar;
import io.github.itzispyder.pdk.Global;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FastCrystals extends Module {

    public FastCrystals() {
        super("fast-crystals", Category.CRYSTAL, "Crystals double the speed! (Another one would detonate right after you punch one)");
    }

    public static void handleCrystal(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity victim = e.getEntity();

        if (!(damager instanceof Player player && victim instanceof EnderCrystal crystal && !CombatUtils.isTaggedSummoned(crystal))) {
            return;
        }

        Profile.get(player).ifEnabled(FastCrystals.class, m -> {
            Block below = crystal.getLocation().add(0, -1, 0).getBlock();
            if (!CrystalAura.isCrystalable(below.getType())) {
                return;
            }
            Hotbar hotbar = Hotbar.from(player);
            Runnable post = () -> CrystalAura.crystalAt(player, hotbar, crystal.getLocation());

            Bukkit.getScheduler().runTaskLater(Global.instance.getPlugin(), post, 2);
            Bukkit.getScheduler().runTaskLater(Global.instance.getPlugin(), post, 4);
        });
    }
}
