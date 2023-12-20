package io.github.itzispyder.combatutils;

import io.github.itzispyder.combatutils.commands.ToggleCmd;
import io.github.itzispyder.combatutils.events.PlayerEvents;
import io.github.itzispyder.combatutils.modules.modules.combat.*;
import io.github.itzispyder.combatutils.modules.modules.crystal.CrystalAura;
import io.github.itzispyder.combatutils.modules.modules.world.AntiKb;
import io.github.itzispyder.combatutils.modules.modules.world.Flight;
import io.github.itzispyder.combatutils.modules.modules.world.NoFall;
import io.github.itzispyder.combatutils.profiles.Profile;
import io.github.itzispyder.pdk.PDK;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatUtils extends JavaPlugin {

    public static final String prefix = "§7[§cC§4U§7]§r ";
    public static final String folder = "plugins/CombatUtils/";

    @Override
    public void onEnable() {
        PDK.init(this);
        this.init();
    }

    @Override
    public void onDisable() {
        Profile.forEach(profile -> {
            profile.saveConfig();
            profile.getConfig().save();
        });
    }

    public void init() {
        // init
        initTicking();

        // listeners
        new PlayerEvents().register();

        // commands
        new ToggleCmd().register();

        // modules
        new Flight().register();
        new Reach().register();
        new Hitboxes().register();
        new KillAura().register();
        new Criticals().register();
        new AutoTotem().register();
        new NoFall().register();
        new AntiKb().register();
        new CrystalAura().register();
    }

    public void initTicking() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> Profile.forEach(Profile::tick), 0, 1);
    }
}
