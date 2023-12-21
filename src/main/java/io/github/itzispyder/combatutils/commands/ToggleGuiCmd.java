package io.github.itzispyder.combatutils.commands;

import io.github.itzispyder.combatutils.profiles.Profile;
import io.github.itzispyder.combatutils.profiles.ProfileGUI;
import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@CommandRegistry(value = "toggle-gui", usage = "/toggle-gui <mode>", playersOnly = true)
public class ToggleGuiCmd implements CustomCommand {

    @Override
    public void dispatchCommand(CommandSender sender, Args args) {
        Player player = (Player) sender;
        Profile profile = Profile.get(player);
        final AtomicReference<ProfileGUI.Mode> mode = new AtomicReference<>(null);
        Consumer<ProfileGUI.Mode> modeSetter = guiMode -> {
            mode.set(guiMode);
            info(sender, "&7Your GUI trigger mode is set to &f" + args.get(0));
        };

        switch (args.get(0).toString()) {
            case "none" ->                  modeSetter.accept(new ProfileGUI.Mode(false, false, false, false));

            case "swap" ->                  modeSetter.accept(new ProfileGUI.Mode(true, false, false, false));
            case "swap-air" ->              modeSetter.accept(new ProfileGUI.Mode(true, true, true, false));
            case "swap-offair" ->           modeSetter.accept(new ProfileGUI.Mode(true, true, false, false));
            case "swap-mainair" ->          modeSetter.accept(new ProfileGUI.Mode(true, false, true, false));

            case "swap-sneaking" ->         modeSetter.accept(new ProfileGUI.Mode(true, false, false, true));
            case "swap-sneaking-air" ->     modeSetter.accept(new ProfileGUI.Mode(true, true, true, true));
            case "swap-sneaking-offair" ->  modeSetter.accept(new ProfileGUI.Mode(true, true, false, true));
            case "swap-sneaking-mainair" -> modeSetter.accept(new ProfileGUI.Mode(true, false, true, true));

            default -> {
                error(sender, "unknown mode.");
            }
        }

        profile.saveConfig();
        profile.getConfig().setGuiMode(mode.get());
        profile.getConfig().save();
    }

    @Override
    public void dispatchCompletions(CompletionBuilder b) {
        b.then(b.arg(
                "none",

                "swap",
                "swap-air",
                "swap-offair",
                "swap-mainair",

                "swap-sneaking",
                "swap-sneaking-air",
                "swap-sneaking-offair",
                "swap-sneaking-mainair"
        ));
    }
}
