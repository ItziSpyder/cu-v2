package io.github.itzispyder.combatutils.profiles;

import io.github.itzispyder.combatutils.CombatUtils;
import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.pdk.utils.ServerUtils;
import io.github.itzispyder.pdk.utils.misc.JsonSerializable;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class Profile {

    private static final Map<UUID, Profile> cache = new HashMap<>();
    private final UUID id;
    private final Config config;
    private final Map<Class<? extends Module>, Module> modules;

    private Profile(UUID id) {
        this.id = id;
        this.modules = Module.cloneRegistry(id);

        String path = "%s/profiles/%s.json".formatted(CombatUtils.folder, id.toString());
        this.config = JsonSerializable.load(path, Config.class, new Config(id));

        loadConfig();
        saveConfig();
        this.config.save();
        cache.put(id, this);
    }

    public void loadConfig() {
        for (Module m : modules.values()) {
            m.setEnabled(config.isEnabled(m));
        }
    }

    public void saveConfig() {
        modules.values().forEach(config::saveModule);
    }

    public Config getConfig() {
        return config;
    }

    public void tick() {
        for (Module m : modules.values()) {
            if (m.isEnabled()) {
                m.onTick();
            }
        }
    }

    public UUID getOwner() {
        return id;
    }

    public Map<Class<? extends Module>, Module> getModules() {
        return new HashMap<>(modules);
    }

    public <T extends Module> T getModule(Class<T> moduleClass) {
        return (T)modules.get(moduleClass);
    }

    public Module getModule(String moduleId) {
        for (Module value : modules.values()) {
            if (value.getId().equalsIgnoreCase(moduleId)) {
                return value;
            }
        }

        moduleId = moduleId.toLowerCase();
        for (Module value : modules.values()) {
            if (value.getId().toLowerCase().contains(moduleId)) {
                return value;
            }
        }
        return null;
    }

    public <T extends Module> void acceptModule(Class<T> moduleClass, Consumer<T> action) {
        T m = getModule(moduleClass);
        if (m != null) {
            action.accept(m);
        }
    }

    public <T extends Module> void ifEnabled(Class<T> moduleClass, Consumer<T> action) {
        T m = getModule(moduleClass);
        if (m != null && m.isEnabled()) {
            action.accept(m);
        }
    }

    public <T extends Module> boolean isEnabled(Class<T> moduleClass) {
        T m = getModule(moduleClass);
        return m != null && m.isEnabled();
    }

    public static Profile get(UUID id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        return new Profile(id);
    }

    public static Profile get(Player sender) {
        return get(sender.getUniqueId());
    }

    public static void forEach(Consumer<Profile> action) {
        ServerUtils.forEachPlayer(player -> action.accept(get(player.getUniqueId())));
    }



    public static class Config implements JsonSerializable<Config> {
        private final UUID id;
        private final Map<String, Boolean> modules = new HashMap<>();

        public Config(UUID id) {
            this.id = id;
        }

        @Override
        public File getFile() {
            return new File("%s/profiles/%s.json".formatted(CombatUtils.folder, id.toString()));
        }

        public void saveModule(Module module) {
            modules.put(module.getId(), module.isEnabled());
        }

        public boolean isEnabled(Module module) {
            if (!modules.containsKey(module.getId())) {
                saveModule(module);
            }
            return modules.get(module.getId());
        }

        public UUID getOwner() {
            return id;
        }

        public Map<String, Boolean> getModules() {
            return new HashMap<>(modules);
        }
    }
}
