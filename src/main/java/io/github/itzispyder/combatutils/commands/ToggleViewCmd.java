package io.github.itzispyder.combatutils.commands;

import io.github.itzispyder.combatutils.profiles.Profile;
import io.github.itzispyder.combatutils.profiles.ProfileGUI;
import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandRegistry(value = "toggle-view", usage = "/toggle-view <player>", playersOnly = true)
public class ToggleViewCmd implements CustomCommand {

    @Override
    public void dispatchCommand(CommandSender sender, Args args) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(args.get(0).toString());
        Profile profile = Profile.get(target.getUniqueId());

        ((Player) sender).openInventory(ProfileGUI.load(profile).getInventory());
    }

    @Override
    public void dispatchCompletions(CompletionBuilder b) {
        b.then(b.arg(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()));
    }
}
