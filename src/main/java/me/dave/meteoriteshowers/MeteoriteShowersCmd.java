package me.dave.meteoriteshowers;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.List;

public class MeteoriteShowersCmd implements CommandExecutor, TabCompleter {
    private final ShowerSummoner showerSummoner = new ShowerSummoner();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("meteoriteshowers.admin.reload")) {
                    sender.sendMessage("§cYou have insufficient permissions.");
                    return true;
                }
                MeteoriteShowers.configManager.reloadConfig();
                PluginDescriptionFile pdf = MeteoriteShowers.getInstance().getDescription();
                sender.sendMessage(ChatColor.GREEN + pdf.getName() + " has been reloaded.");
                return true;
            }
            if (args[0].equalsIgnoreCase("shoot")) {
                if (!sender.hasPermission("meteoriteshowers.admin.shoot")) {
                    sender.sendMessage("§cYou have insufficient permissions.");
                    return true;
                }
                showerSummoner.summonPlayerMeteorites(0, 0);
                return true;
            }
        }
        PluginDescriptionFile pdf = MeteoriteShowers.getInstance().getDescription();
        sender.sendMessage("You are currently running " + pdf.getName() + " Version: " + pdf.getVersion());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {

        List<String> tabComplete = new ArrayList<>();
        List<String> wordCompletion = new ArrayList<>();
        boolean wordCompletionSuccess = false;

        if (args.length == 1) {
            if (commandSender.hasPermission("meteoriteshowers.admin.reload")) tabComplete.add("reload");
            if (commandSender.hasPermission("meteoriteshowers.admin.shoot")) tabComplete.add("shoot");
        }

        for (String currTab : tabComplete) {
            int currArg = args.length - 1;
            if (currTab.startsWith(args[currArg])) {
                wordCompletion.add(currTab);
                wordCompletionSuccess = true;
            }
        }
        if (wordCompletionSuccess) return wordCompletion;
        return tabComplete;
    }
}
