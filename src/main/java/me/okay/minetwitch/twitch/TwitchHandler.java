package me.okay.minetwitch.twitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.helix.domain.UserList;

import me.okay.minetwitch.MinetwitchPlugin;
import me.okay.minetwitch.utils.TextFormat;

public class TwitchHandler implements Listener {

    private MinetwitchPlugin plugin;
    private String channelName;
    private TwitchClient twitchClient;
    private TwitchBot twitchBot;
    private List<TwitchCommand> commands = new ArrayList<>();

    public TwitchHandler(MinetwitchPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // chat credentials
        OAuth2Credential credentials = new OAuth2Credential("twitch", plugin.getConfig().getString("twitch-info.token"));

        TwitchIdentityProvider identityProvider = new TwitchIdentityProvider(null, null, null);

        if (!identityProvider.isCredentialValid(credentials).get()) {
            plugin.getLogger().warning("Invalid Twitch token! Please check your config.yml and reload.");
            channelName = null;
            return;
        }
    
        // twitch client
        twitchClient = TwitchClientBuilder.builder()
            .withEnableChat(true)
            .withChatAccount(credentials)
            .withEnableHelix(true)
            .withEventManager(plugin.getTwitch4jEventManager())
            .build();
            
        channelName = plugin.getConfig().getString("twitch-info.channel-name");
        if (channelName.length() == 0) {
            channelName = identityProvider.getAdditionalCredentialInformation(credentials).map(OAuth2Credential::getUserName).orElse(null);
        }
        else {
            UserList resultList = twitchClient.getHelix().getUsers(credentials.getAccessToken(), null, Arrays.asList(channelName)).execute();
            if (resultList.getUsers().size() == 0) {
                plugin.getLogger().warning("Invalid Twitch channel name! Please check your config.yml and reload.");
                return;
            }
        }
        
        plugin.getLogger().info("Connecting to Twitch channel: " + channelName);
        twitchClient.getChat().joinChannel(channelName);
    
        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, this::onChannelMessage);
        twitchBot = new TwitchBot(twitchClient, channelName);
    }

    private void onChannelMessage(final ChannelMessageEvent event) {
        if (!plugin.getConfig().getBoolean("share-chat.twitch-to-minecraft.enabled")) {
            return;
        }

        boolean shareMessage = true;
        if (event.getMessage().startsWith(plugin.getCommandPrefix())) {
            shareMessage = !plugin.getConfig().getBoolean("share-chat.twitch-to-minecraft.ignore-commands");
            
            // get first space index
            int spaceIndex = event.getMessage().indexOf(' ');
            if (spaceIndex == -1) {
                spaceIndex = event.getMessage().length();
            }

            String commandString = event.getMessage().substring(plugin.getCommandPrefix().length(), spaceIndex);
            String pluginName, commandName;

            if (commandString.contains(":")) {
                int colonIndex = commandString.indexOf(':');

                // assign plugin to everything before the colon
                pluginName = commandString.substring(0, colonIndex);

                // assign commandName to everything after the colon
                commandName = commandString.substring(colonIndex + 1);
            }
            else {
                pluginName = null;
                commandName = commandString;
            }

            for (TwitchCommand command : commands) {
                if ((pluginName == null || command.getPlugin().getName().toLowerCase().equals(pluginName)) && (command.getName().equals(commandName) || command.getAliases().contains(commandName))) {
                    // split rest of message into arguments without any leading/trailing spaces
                    String argString = event.getMessage().substring(spaceIndex).trim();
                    String[] args = argString.split(" ");
                    if (args.length == 1 && args[0].length() == 0) {
                        args = new String[0];
                    }

                    command.execute(twitchBot, event, args, commandName);
                    break;
                }
            }
        }

        if (shareMessage) {
            Optional<UUID> minecraftUuid = plugin.getLinkedAccountsManager().getMinecraftUuid(Integer.parseInt(event.getUser().getId()));

            String message = plugin.getConfig().getString(
                minecraftUuid.isPresent() ? "share-chat.twitch-to-minecraft.format.linked" : "share-chat.twitch-to-minecraft.format.unlinked"
            );
            
            message = message.replaceAll("%twitch%", event.getUser().getName());
            message = message.replaceAll("%twitch-color%", "&" + event.getMessageEvent().getTagValue("color").orElse("4"));
            
            message = TextFormat.formatHexCodes(message);
            message = TextFormat.colorize(message);
            message = message.replaceAll("%message%", event.getMessage());
    
            // Bukkit.getServer().broadcastMessage(message);
            // Send message to all players who have permission
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("minetwitch.broadcast.twitch-to-minecraft")) {
                    player.sendMessage(message);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onChat(final AsyncPlayerChatEvent event) {
        if (!plugin.getConfig().getBoolean("share-chat.minecraft-to-twitch.enabled")) {
            return;
        }

        // Do not share if player does not have permission
        if (!event.getPlayer().hasPermission("minetwitch.broadcast.minecraft-to-twitch")) {
            return;
        }

        Optional<Integer> twitchId = plugin.getLinkedAccountsManager().getTwitchId(event.getPlayer());

        String message = plugin.getConfig().getString(
            twitchId.isPresent() ?  "share-chat.minecraft-to-twitch.format.linked" : "share-chat.minecraft-to-twitch.format.unlinked"
        );
        message = message.replaceAll("%player%", event.getPlayer().getName());
        message = message.replaceAll("%message%", event.getMessage());

        twitchClient.getChat().sendMessage(channelName, message);
    }

    public void disconnect() {
        plugin.getLogger().info("Disconnecting from Twitch...");

        if (twitchClient != null) {
            twitchClient.getChat().leaveChannel(channelName);
            twitchClient.close();
        }
        twitchBot = null;

        plugin.getLogger().info("Disconnected from Twitch.");
        
        AsyncPlayerChatEvent.getHandlerList().unregister(this);
    }

    public void registerCommand(TwitchCommand command) {
        commands.add(command);
    }

    public boolean unregisterCommand(TwitchCommand command) {
        return commands.remove(command);
    }
}

