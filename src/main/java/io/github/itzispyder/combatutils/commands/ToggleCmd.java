package io.github.itzispyder.combatutils.commands;

import io.github.itzispyder.combatutils.modules.Module;
import io.github.itzispyder.combatutils.profiles.Profile;
import io.github.itzispyder.combatutils.profiles.ProfileGUI;
import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
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

        if (len == 0) {
            player.openInventory(ProfileGUI.load(profile).getInventory());
        }
        else if (len == 1) {
            module.toggle();
            module.notifyToggle(player);
        }
        else if (len == 2) {
            module.setEnabled(args.get(1).toBool());
            module.notifyToggle(player);
        }
        else {
            error(sender, "unknown command.");
        }
    }

    @Override
    public void dispatchCompletions(CompletionBuilder b) {
        b.then(b.arg(Module.getRegistry().values().stream().map(Module::getId).toList())
                .then(b.arg("true", "false")));
    }
}
