package com.rojel.wesv;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandWesv implements TabExecutor {

    private final WorldEditSelectionVisualizer plugin;

    public CommandWesv(WorldEditSelectionVisualizer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("wesv.use")) {
            sender.sendMessage(plugin.getCustomConfig().getLangNoPermission());
            return true;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("reload") || !sender.hasPermission("wesv.reloadconfig")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                boolean isEnabled = !plugin.getStorageManager().isEnabled(player);
                plugin.getStorageManager().setEnable(player, isEnabled);

                if (isEnabled) {
                    player.sendMessage(ChatColor.GREEN + plugin.getCustomConfig().getLangVisualizerEnabled());
                    if (plugin.shouldShowSelection(player)) {
                        plugin.showSelection(player);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + plugin.getCustomConfig().getLangVisualizerDisabled());
                    plugin.hideSelection(player);
                }
            } else {
                sender.sendMessage(plugin.getCustomConfig().getLangPlayersOnly());
            }
        } else {
            plugin.getCustomConfig().reloadConfig(true);
            sender.sendMessage(plugin.getCustomConfig().getConfigReloaded());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission("wesv.reloadconfig")) {
            return StringUtil.copyPartialMatches(args[0], Collections.singletonList("reload"), new ArrayList<>());
        }

        return Collections.emptyList();
    }
}
