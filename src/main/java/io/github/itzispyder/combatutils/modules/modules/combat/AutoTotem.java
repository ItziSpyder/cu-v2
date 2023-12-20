package io.github.itzispyder.combatutils.modules.modules.combat;

import io.github.itzispyder.combatutils.modules.Category;
import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.combatutils.profiles.Profile;
import io.github.itzispyder.pdk.Global;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class AutoTotem extends Module {

    public AutoTotem() {
        super("auto-totem", Category.PVP, "Auto totem when popped.");
    }

    public static void handleDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }

        Profile.get(player).ifEnabled(AutoTotem.class, m -> {
            if (player.getInventory().contains(Material.TOTEM_OF_UNDYING) && player.getHealth() - e.getFinalDamage() <= 0) {
                replenishTotem(player);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Global.instance.getPlugin(), () -> replenishTotem(player),1);
            }
        });
    }

    private static void replenishTotem(Player p) {
        Material totem = Material.TOTEM_OF_UNDYING;
        PlayerInventory inv = p.getInventory();
        
        for (ItemStack item : inv.getContents()) {
            if (item == null || !item.getType().equals(totem)) {
                continue;
            }
            if (inv.getItemInOffHand().getType().isAir() && !inv.getItemInMainHand().getType().equals(totem)) {
                inv.setItemInOffHand(new ItemStack(totem));
                item.setAmount(item.getAmount() - 1);
                p.updateInventory();
                break;
            }
            else if (inv.getItemInMainHand().getType().isAir() && !inv.getItemInOffHand().getType().equals(totem)) {
                inv.setItemInMainHand(new ItemStack(totem));
                item.setAmount(item.getAmount() - 1);
                p.updateInventory();
                break;
            }
        }
    }
}
