package me.dave.meteoriteshowers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MeteoriteShowersCmd implements CommandExecutor, TabCompleter {
    private final MeteoriteSummoner meteoriteSummoner = new MeteoriteSummoner();
    private final PluginDescriptionFile pdf = MeteoriteShowers.getInstance().getDescription();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("meteoriteshowers.admin.reload")) {
                    sender.sendMessage("§cYou have insufficient permissions.");
                    return true;
                }
                MeteoriteShowers.configManager.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + pdf.getName() + " has been reloaded.");
                return true;
            }
            if (args[0].equalsIgnoreCase("shoot")) {
                if (!sender.hasPermission("meteoriteshowers.admin.shoot")) {
                    sender.sendMessage("§cYou have insufficient permissions.");
                    return true;
                }
                meteoriteSummoner.summonPlayerMeteorites(0, 0);
                return true;
            }
        }
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("shoot")) {
                switch(args[1]) {
                    case "player" -> {
                        if (args.length >= 3) {
                            int duration = 0;
                            int count = 0;

                            try {
                                if (args.length >= 4) duration = Integer.parseInt(args[3]) * 20;
                                if (args.length >= 5) count = Integer.parseInt(args[4]);
                            } catch(Exception err) {
                                sender.sendMessage("§cIncorrect usage: Try /ms shoot player <player>");
                            }

                            List<UUID> playerList = new ArrayList<>();
                            Player target = Bukkit.getPlayer(args[2]);
                            if (target == null) {
                                sender.sendMessage("§cIncorrect usage: Try /ms shoot player <player>");
                                return true;
                            }
                            playerList.add(target.getUniqueId());
                            meteoriteSummoner.summonPlayerMeteorites(playerList, duration, count);
                            return true;
                        }
                    }
                    case "all" -> {
                        int duration = 0;
                        int count = 0;

                        try {
                            if (args.length >= 3) duration = Integer.parseInt(args[2]) * 20;
                            if (args.length >= 4) count = Integer.parseInt(args[3]);
                        } catch(Exception err) {
                            sender.sendMessage("§cIncorrect usage: Try /ms shoot all <duration> <count>");
                        }

                        meteoriteSummoner.summonPlayerMeteorites(duration, count);
                        return true;
                    }
                    case "coord" -> {
                        if (args.length >= 6) {
                            int duration = 0;
                            int count = 0;

                            try {
                                if (args.length >= 7) duration = Integer.parseInt(args[6]) * 20;
                                if (args.length >= 8) count = Integer.parseInt(args[7]);
                            } catch(NumberFormatException err) {
                                sender.sendMessage("§cIncorrect usage: Try /ms shoot coord <world> <x> <y> <z> <duration> <count>");
                            }

                            meteoriteSummoner.summonLocationMeteorites(new Location(Bukkit.getWorld(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5])), duration, count);
                            return true;
                        }
                    }
                    case "range" -> {
                        if (args.length >= 9) {
                            int duration = 0;
                            int count = 0;

                            try {
                                if (args.length >= 10) duration = Integer.parseInt(args[9]) * 20;
                                if (args.length >= 11) count = Integer.parseInt(args[10]);
                            } catch(NumberFormatException err) {
                                sender.sendMessage("§cIncorrect usage: Try /ms shoot coord <world> <x> <y> <z> <duration> <count>");
                            }

                            Location corner1 = new Location(Bukkit.getWorld(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                            Location corner2 = new Location(Bukkit.getWorld(args[2]), Double.parseDouble(args[6]), Double.parseDouble(args[7]), Double.parseDouble(args[8]));

                            meteoriteSummoner.summonRangeMeteorites(corner1, corner2, duration, count);
                            return true;
                        }
                    }
                }
            }
        }
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
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("shoot")) {
                if (commandSender.hasPermission("meteoriteshowers.admin.shoot")) {
                    switch (args.length) {
                        case 2 -> {
                            tabComplete.add("player");
                            tabComplete.add("all");
                            tabComplete.add("coord");
                            tabComplete.add("range");
                        }
                        case 3 -> {
                            switch(args[1]) {
                                case "player" -> { return null; }
                                case "all" -> tabComplete.add("<duration>");
                                case "coord", "range" -> Bukkit.getWorlds().forEach((world) -> tabComplete.add(world.getName()));
                            }
                        }
                        case 4 -> {
                            switch(args[1]) {
                                case "player" -> tabComplete.add("<duration>");
                                case "all" -> tabComplete.add("<count>");
                                case "coord", "range" -> {
                                    if (commandSender instanceof Player player) tabComplete.add(String.valueOf(player.getLocation().getBlockX()));
                                }
                            }
                        }
                        case 5 -> {
                            switch(args[1]) {
                            case "player" -> tabComplete.add("<count>");
                            case "coord", "range" -> {
                                if (commandSender instanceof Player player) tabComplete.add(String.valueOf(player.getLocation().getBlockY()));
                            }
                        }}
                        case 6 -> {
                            switch(args[1]) {
                                case "coord", "range" -> {
                                    if (commandSender instanceof Player player) tabComplete.add(String.valueOf(player.getLocation().getBlockZ()));
                                }
                            }
                        }
                        case 7 -> {
                            switch(args[1]) {
                                case "coord" -> tabComplete.add("<duration>");
                                case "range" -> {
                                    if (commandSender instanceof Player player) tabComplete.add(String.valueOf(player.getLocation().getBlockX()));
                                }
                            }
                        }
                        case 8 -> {
                            switch(args[1]) {
                                case "coord" -> tabComplete.add("<count>");
                                case "range" -> {
                                    if (commandSender instanceof Player player) tabComplete.add(String.valueOf(player.getLocation().getBlockY()));
                                }
                            }
                        }
                        case 9 -> {
                            if (args[1].equals("range")) {
                                if (commandSender instanceof Player player) tabComplete.add(String.valueOf(player.getLocation().getBlockZ()));
                            }
                        }
                        case 10 -> {
                            if (args[1].equals("range")) tabComplete.add("<duration>");
                        }
                        case 11 -> {
                            if (args[1].equals("range")) tabComplete.add("<count>");
                        }
                    }
                }
            }
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
