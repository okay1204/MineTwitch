package me.okay.minetwitch;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.okay.minetwitch.command.MinetwitchCommand;
public class Minetwitch extends JavaPlugin {
    private TwitchBot twitchBot;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        twitchBot = new TwitchBot(this);
        
        // Commands
        new MinetwitchCommand(this);
    }

    @Override
    public void onDisable() {
        twitchBot.disconnect();
    }

    public void reloadTwitchBot() {
        twitchBot.disconnect();
        twitchBot = new TwitchBot(this);
    }
}