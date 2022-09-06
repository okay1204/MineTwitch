package me.okay.minetwitch;

import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;

import me.okay.minetwitch.command.MinetwitchCommand;
import me.okay.minetwitch.twitch.TwitchCommand;
import me.okay.minetwitch.twitch.TwitchHandler;
import me.okay.minetwitch.twitch.builtincommands.Link;
public class Minetwitch extends JavaPlugin {
    private TwitchHandler twitchHandler;
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

        twitchHandler = new TwitchHandler(this);

        MinetwitchApi.registerCommand(new TwitchCommand(this, "link", Set.of(), new Link()));
        
        // Commands
        new MinetwitchCommand(this);
    }

    @Override
    public void onDisable() {
        twitchHandler.disconnect();
        twitch4jEventManager.close();
    }

    public Database getDatabase() {
        return database;
    }

    public TwitchHandler getTwitchHandler() {
        return twitchHandler;
    }

    public EventManager getTwitch4jEventManager() {
        return twitch4jEventManager;
    }

    public void reloadTwitchBot() {
        twitchHandler.disconnect();
        twitchHandler = new TwitchHandler(this);
    }
}