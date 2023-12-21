package io.github.itzispyder.combatutils.modules.modules.world;

import io.github.itzispyder.combatutils.modules.Category;
import io.github.itzispyder.combatutils.modules.Module;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class Speed extends Module {

    private static final double defWalkAttr = 0.10000000149011612;
    private static final float defFlySpeed = 0.05F;

    public Speed() {
        super("speed", Category.WORLD, "Makes you move faster.");
    }

    @Override
    protected void onEnable() {
        getOwningPlayer().accept(player -> {
            setWalkAttr(player, 0.6);
            player.setFlySpeed(0.6F);
        });
    }

    @Override
    protected void onDisable() {
        getOwningPlayer().accept(player -> {
            setWalkAttr(player, defWalkAttr);
            player.setFlySpeed(defFlySpeed);
        });
    }

    private void setWalkAttr(Player player, double base) {
        var move = Attribute.GENERIC_MOVEMENT_SPEED;
        var attr = player.getAttribute(move);

        if (attr == null) {
            player.registerAttribute(move);
            attr = player.getAttribute(move);
        }

        attr.setBaseValue(base);
    }
}
