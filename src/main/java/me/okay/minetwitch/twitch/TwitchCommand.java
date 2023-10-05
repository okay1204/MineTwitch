package me.okay.minetwitch.twitch;

import java.util.Set;

import org.bukkit.plugin.Plugin;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public class TwitchCommand {
    private Plugin plugin;
    private String name;
    private TwitchCommandExecutor executor;
    private Set<String> aliases;

    public TwitchCommand(Plugin plugin, String name, TwitchCommandExecutor executor) {
        this(plugin, name, executor, Set.of());
    }

    public TwitchCommand(Plugin plugin, String name, TwitchCommandExecutor executor, Set<String> aliases) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        this.plugin = plugin;

        if (name == null || name.isEmpty() || name.contains(" ") || name.contains(":")) {
            throw new IllegalArgumentException("Name cannot be null or empty, and cannot contain spaces or colons");
        }
        this.name = name;
        
        if (executor == null) {
            throw new IllegalArgumentException("Executor cannot be null");
        }
        this.executor = executor;
        
        this.aliases = aliases;
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
