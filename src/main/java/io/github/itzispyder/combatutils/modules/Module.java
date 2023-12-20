package io.github.itzispyder.combatutils.modules;

import io.github.itzispyder.combatutils.CombatUtils;
import io.github.itzispyder.pdk.Global;
import io.github.itzispyder.pdk.utils.StringUtils;
import io.github.itzispyder.pdk.utils.misc.Voidable;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Module implements Global, Cloneable {

    private static final Map<Class<? extends Module>, Module> registry = new HashMap<>();
    private final String id, name, desc;
    private final Category category;
    private Voidable<UUID> owner = Voidable.of(null);
    private boolean enabled = false;

    protected Module(String name, Category category, String desc) {
        this.id = name.toLowerCase().replaceAll("[^a-z0-9-]", "-");
        this.name = StringUtils.capitalizeWords(id);
        this.category = category;
        this.desc = desc;
    }

    protected void onEnable() {

    }

    protected void onDisable() {

    }

    public void onTick() {

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            onEnable();
        }
        else {
            onDisable();
        }
    }

    public Voidable<UUID> getOwner() {
        return owner;
    }

    public Voidable<Player> getOwningPlayer() {
        if (!owner.isPresent()) {
            return Voidable.of(null);
        }

        Player p = Bukkit.getPlayer(owner.get());
        if (p == null || !p.isOnline() || p.isDead()) {
            return Voidable.of(null);
        }

        return Voidable.of(p);
    }

    public void setOwner(UUID owner) {
        this.owner = Voidable.of(owner);
    }

    public void setOwner(Player owner) {
        setOwner(owner.getUniqueId());
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public void notifyToggle(CommandSender sender) {
        String status = enabled ? "enabled" : "disabled";
        info(sender, "%s&f%s&7 is now &f%s".formatted(CombatUtils.prefix, name, status));
    }

    @Override
    protected Module clone() {
        try {
            Object obj = super.clone();
            if (!(obj instanceof Module m)) {
                return null;
            }
            m.enabled = false;
            return m;
        }
        catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    public void register() {
        registry.put(this.getClass(), this);
    }

    public static Map<Class<? extends Module>, Module> getRegistry() {
        return new HashMap<>(registry);
    }

    public static Map<Class<? extends Module>, Module> cloneRegistry(UUID owner) {
        return new HashMap<>() {{
            for (Module m : registry.values()) {
                m.setOwner(owner);
                this.put(m.getClass(), m.clone());
            }
        }};
    }
}
