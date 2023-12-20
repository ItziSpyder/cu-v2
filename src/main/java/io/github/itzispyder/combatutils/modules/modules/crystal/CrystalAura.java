package io.github.itzispyder.combatutils.modules.modules.crystal;

import io.github.itzispyder.combatutils.modules.Category;
import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.combatutils.util.Hotbar;
import io.github.itzispyder.pdk.utils.misc.SoundPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

public class CrystalAura extends Module {

    public CrystalAura() {
        super("crystal-aura", Category.CRYSTAL, "Auto crystals entities around you.");
    }

    @Override
    public void onTick() {
        getOwningPlayer().accept(player -> {
            Hotbar hotbar = Hotbar.from(player);
            Location loc = player.getLocation();
            BoundingBox box = player.getBoundingBox().expand(6);

            for (int x = (int)box.getMinX(); x <= (int)box.getMaxX(); x++) {
                for (int y = (int)box.getMinY(); y <= (int)box.getMaxY(); y++) {
                    for (int z = (int)box.getMinZ(); z <= (int)box.getMaxZ(); z++) {
                        Location bLoc = new Location(loc.getWorld(), x, y, z);
                        boolean shouldStop = checkEntities(player, hotbar, bLoc);

                        if (shouldStop) {
                            return;
                        }
                    }
                }
            }
        });
    }

    private boolean checkEntities(Player player, Hotbar hotbar, Location bLoc) {
        Block block = bLoc.getBlock();

        for (Entity entity : bLoc.getWorld().getNearbyEntities(bLoc,2,2,2)) {
            if (entity != player && entity instanceof LivingEntity && !entity.isDead() && hotbar.containsItem(Material.END_CRYSTAL)) {
                Block underBlock = entity.getLocation().clone().add(0,-1,0).getBlock();
                Block feetBlock = entity.getLocation().clone().getBlock();

                if (isCrystalable(underBlock.getType()) && feetBlock.getType().equals(Material.AIR)) {
                    hotbar.deductItem(Material.END_CRYSTAL, false);
                    crystalAt(bLoc.add(0, 1, 0));
                    return true;
                }
                if (block.getType().equals(Material.AIR) && hotbar.containsItem(Material.OBSIDIAN)) {
                    hotbar.deductItem(Material.OBSIDIAN, false);
                    block.setType(Material.OBSIDIAN);
                    SoundPlayer place = new SoundPlayer(bLoc, Sound.BLOCK_STONE_PLACE, 1, 0.7F);
                    place.playWithin(5);
                }
                if (isCrystalable(block.getType()) && bLoc.clone().add(0,1,0).getBlock().getType().equals(Material.AIR)) {
                    hotbar.deductItem(Material.END_CRYSTAL, false);
                    crystalAt(bLoc.add(0, 1, 0));
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isCrystalable(Material type) {
        return type == Material.OBSIDIAN || type == Material.BEDROCK;
    }

    private void crystalAt(Location loc) {
        loc = loc.getBlock().getLocation().add(0.5, 0, 0.5);
        EnderCrystal ent = loc.getWorld().spawn(loc, EnderCrystal.class);
        ent.setShowingBottom(false);

        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> getOwningPlayer().accept(player -> {
            if (!ent.isDead()) {
                player.attack(ent);
            }
        }),1);
    }
}
