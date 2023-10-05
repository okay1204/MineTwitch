package me.okay.minetwitch;

import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;

import me.okay.minetwitch.command.MinetwitchCommand;
import me.okay.minetwitch.data.LinkedAccounts;
import me.okay.minetwitch.data.database.Database;
import me.okay.minetwitch.data.database.MySQL;
import me.okay.minetwitch.data.database.SQLite;
import me.okay.minetwitch.linkcode.LinkCodeManager;
import me.okay.minetwitch.twitch.TwitchCommand;
import me.okay.minetwitch.twitch.TwitchHandler;
import me.okay.minetwitch.twitch.builtincommands.Link;

public class MinetwitchPlugin extends JavaPlugin {
    private TwitchHandler twitchHandler;
    private Database database;
    private LinkedAccounts linkedAccountsManager;
    private LinkCodeManager linkCodeManager;

    private EventManager twitch4jEventManager;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        database = getConfig().getBoolean("mysql.enabled") ? new MySQL(this) : new SQLite(this);

        // This is here to work around a strange issue that has to do with random errors with the twitch4j library and spigot.
        twitch4jEventManager = new EventManager();
        twitch4jEventManager.registerEventHandler(new SimpleEventHandler());
        twitch4jEventManager.setDefaultEventHandler(SimpleEventHandler.class);

        linkedAccountsManager = new LinkedAccounts(this);

        linkCodeManager = new LinkCodeManager(this);

        twitchHandler = new TwitchHandler(this);

        twitchHandler.registerCommand(new TwitchCommand(this, "link", Set.of(), new Link(this)));
        
        // Commands
        new MinetwitchCommand(this);
    }

    @Override
    public void onDisable() {
        twitchHandler.disconnect();
        // twitch4jEventManager.close();
        linkedAccountsManager.saveCache();
        database.safeDisconnect();
    }

    public Database getDatabase() {
        return database;
    }

    public LinkedAccounts getLinkedAccountsManager() {
        return linkedAccountsManager;
    }

    public TwitchHandler getTwitchHandler() {
        return twitchHandler;
    }

    public EventManager getTwitch4jEventManager() {
        return twitch4jEventManager;
    }

    public LinkCodeManager getLinkCodeManager() {
        return linkCodeManager;
    }

    public String getCommandPrefix() {
        return getConfig().getString("twitch-command-prefix", "!");
    }

    public void reloadTwitchBot() {
        twitchHandler.disconnect();
        twitchHandler = new TwitchHandler(this);
    }
}