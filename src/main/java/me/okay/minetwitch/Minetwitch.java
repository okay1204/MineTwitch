package me.okay.minetwitch;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;

import me.okay.minetwitch.command.MinetwitchCommand;
public class Minetwitch extends JavaPlugin {
    private TwitchBot twitchBot;
    private Database database;

    private EventManager twitch4jEventManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // This is here to work around a strange issue that has to do with random errors with the twitch4j library and spigot.
        twitch4jEventManager = new EventManager();
        twitch4jEventManager.registerEventHandler(new SimpleEventHandler());
        twitch4jEventManager.setDefaultEventHandler(SimpleEventHandler.class);

        database = new Database(this);

        twitchBot = new TwitchBot(this);
        
        // Commands
        new MinetwitchCommand(this);
    }

    @Override
    public void onDisable() {
        twitchBot.disconnect();
        twitch4jEventManager.close();
    }

    public Database getDatabase() {
        return database;
    }

    public void reloadTwitchBot() {
        twitchBot.disconnect();
        twitchBot = new TwitchBot(this);
    }
}