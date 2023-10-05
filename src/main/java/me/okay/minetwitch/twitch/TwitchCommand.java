package me.okay.minetwitch.twitch;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.plugin.Plugin;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public class TwitchCommand {
    private Plugin plugin;
    private String name;
    private Set<String> aliases;
    private TwitchCommandExecutor executor;

    public TwitchCommand(Plugin plugin, String name, Set<String> aliases, TwitchCommandExecutor executor) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        this.plugin = plugin;

        if (name == null || name.isEmpty() || name.contains(" ") || name.contains(":")) {
            throw new IllegalArgumentException("Name cannot be null or empty, and cannot contain spaces or colons");
        }
        this.name = name;

        if (aliases == null) {
            aliases = new HashSet<>();
        }
        this.aliases = new HashSet<>(aliases);

        if (executor == null) {
            throw new IllegalArgumentException("Executor cannot be null");
        }
        this.executor = executor;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public TwitchCommandExecutor getExecutor() {
        return executor;
    }

    public void execute(TwitchBot twitchBot, ChannelMessageEvent messageEvent, String[] args, String label) {
        executor.execute(twitchBot, messageEvent, args, label, this);
    }
}
