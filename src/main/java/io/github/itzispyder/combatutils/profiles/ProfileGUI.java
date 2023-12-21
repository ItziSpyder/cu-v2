package io.github.itzispyder.combatutils.profiles;

import io.github.itzispyder.combatutils.CombatUtils;
import io.github.itzispyder.combatutils.modules.Category;
import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.pdk.Global;
import io.github.itzispyder.pdk.plugin.builders.ItemBuilder;
import io.github.itzispyder.pdk.plugin.gui.CustomGui;
import io.github.itzispyder.pdk.utils.StringUtils;
import io.github.itzispyder.pdk.utils.misc.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProfileGUI {

    private static final CustomGui.CreateAction ON_DEFINE = inv -> {
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
    };

    public static CustomGui load(Profile profile) {
        Function<Category, Predicate<Module>> filter = c -> m -> m.getCategory() == c;
        Comparator<Module> comparator = Comparator.comparing(Module::getId);
        Function<Category, Module[]> moduleFilter = c -> profile.getModules().values().stream().filter(filter.apply(c)).sorted(comparator).toArray(Module[]::new);
        Category[] categories = Category.values();

        var gui = CustomGui.create()
            .title(CombatUtils.prefix + "§7§o" + Bukkit.getOfflinePlayer(profile.getOwner()).getName())
            .size(54)
            .onDefine(ON_DEFINE)
            .onClose(inv -> {
                profile.saveConfig();
                profile.getConfig().save();
            })
            .defineMain(e -> {
                e.setCancelled(true);
                Bukkit.getScheduler().runTaskLater(Global.instance.getPlugin(), () -> e.getWhoClicked().openInventory(load(profile).getInventory()), 1);
            });

        for (int i = 0; i < categories.length; i++) {
            Category c = categories[i];
            ItemStack cSlot = ItemBuilder.create()
                    .material(c.getIcon())
                    .flag(ItemFlag.HIDE_ITEM_SPECIFICS, ItemFlag.HIDE_ATTRIBUTES)
                    .name("§e" + StringUtils.capitalizeWords(c.name()))
                    .build();
            gui.define(i, cSlot);

            int index = i + 9;
            for (Module m : moduleFilter.apply(c)) {
                var slot = createModuleSlot(m);
                gui.define(index, slot.left, slot.right);
                index += 9;
            }
        }

        return gui.build();
    }

    private static Pair<ItemStack, CustomGui.InvAction> createModuleSlot(Module module) {
        boolean on = module.isEnabled();
        var item = ItemBuilder.create();

        item.material(on ? Material.WARPED_SIGN : Material.CRIMSON_SIGN);
        item.name((on ? "§b" : "§7") + module.getName());

        for (String line : StringUtils.wrapLines(module.getDesc(), 20, true)) {
            item.lore("§8" + line);
        }

        item.flag(ItemFlag.HIDE_ITEM_SPECIFICS);
        return Pair.of(item.build(), e -> module.toggle());
    }

    public static void handleToggle(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        Profile profile = Profile.get(player);

        if (profile.getConfig().getGuiMode().test(player)) {
            e.setCancelled(true);
            player.openInventory(load(profile).getInventory());
        }
    }

    public record Mode(boolean swap, boolean offhandAir, boolean mainhandAir, boolean sneak) {
        public boolean test(Player player) {
            if (player == null) {
                return false;
            }
            PlayerInventory inv = player.getInventory();

            if (offhandAir && !inv.getItemInOffHand().isEmpty()) {
                return false;
            }
            if (mainhandAir && !inv.getItemInMainHand().isEmpty()) {
                return false;
            }
            if (sneak && !player.isSneaking()) {
                return false;
            }
            return swap;
        }
    }
}
