package me.okay.minetwitch;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import me.okay.minetwitch.twitch.TwitchCommand;

public class Minetwitch {
    private static MinetwitchPlugin getPlugin() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        MinetwitchPlugin plugin = (MinetwitchPlugin) pluginManager.getPlugin("MineTwitch");

        if (plugin == null) {
            throw new IllegalStateException("MineTwitch plugin is not loaded");
        }

        return plugin;
    }

    public static void registerCommand(TwitchCommand command) {
        getPlugin().getTwitchHandler().registerCommand(command);
    }

    public static boolean unregisterCommand(TwitchCommand command) {
        return getPlugin().getTwitchHandler().unregisterCommand(command);
    }
}
