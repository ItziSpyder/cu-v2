package io.github.itzispyder.combatutils.modules;

import org.bukkit.Material;

public enum Category {

    PVP(Material.DIAMOND_SWORD),
    CRYSTAL(Material.END_CRYSTAL),
    WORLD(Material.GRASS_BLOCK);

    private final Material icon;

    Category(Material icon) {
        this.icon = icon;
    }

    public Material getIcon() {
        return icon;
    }
}
