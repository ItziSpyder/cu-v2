package io.github.itzispyder.combatutils.commands;

import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.combatutils.profiles.Profile;
import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandRegistry(value = "toggle", usage = "/toggle <module id> <bool enabled>", playersOnly = true)
public class ToggleCmd implements CustomCommand {

    @Override
    public void dispatchCommand(CommandSender sender, Args args) {
        Player player = (Player) sender;
        Profile profile = Profile.get(player);
        Module module = profile.getModule(args.get(0).toString());
        int len = args.getSize();
        
        if (len == 1) {
            module.toggle();
            module.notifyToggle(player);
        }
        else if (len == 2) {
            module.setEnabled(args.get(1).toBool());
            module.notifyToggle(player);
        }
        else if (len == 4) {
            player = Bukkit.getPlayer(args.get(3).toString());
            profile = Profile.get(player);
            module = profile.getModule(args.get(0).toString());

            module.setEnabled(args.get(1).toBool());
            module.notifyToggle(player);

            info(sender, "&7Toggled for &f" + player.getName());
        }
        else {
            error(sender, "unknown command.");
        }
    }

    @Override
    public void dispatchCompletions(CompletionBuilder b) {
        b.then(b.arg(Module.getRegistry().values().stream().map(Module::getId).toList())
                .then(b.arg("true", "false")
                        .then(b.arg("for")
                                .then(b.arg(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList())))));
    }
}
