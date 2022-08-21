package me.okay.minetwitch;

import java.util.Arrays;

import org.bukkit.Bukkit;
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

import me.okay.minetwitch.utils.ColorFormat;

public class TwitchBot implements Listener {
    private Minetwitch plugin;
    private String channelName;
    private TwitchClient twitchClient;

    public TwitchBot(Minetwitch plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // chat credentials
        OAuth2Credential credentials = new OAuth2Credential("twitch", plugin.getConfig().getString("twitch-info.token", ""));

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
            .build();
    
            
        channelName = plugin.getConfig().getString("twitch-info.channel-name", "");
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
    
        if (plugin.getConfig().getBoolean("share-chat.twitch-to-minecraft.enabled")) {
            twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, this::onChannelMessage);
        }
    }

    private void onChannelMessage(final ChannelMessageEvent event) {
        String message = plugin.getConfig().getString("share-chat.twitch-to-minecraft.format.unlinked");
        
        message = message.replaceAll("%twitch%", event.getUser().getName());
        message = message.replaceAll("%twitch-color%", "&" + event.getMessageEvent().getTagValue("color").orElse("4"));
        
        message = ColorFormat.formatHexCodes(message);
        message = ColorFormat.colorize(message);
        message = message.replaceAll("%message%", event.getMessage());

        Bukkit.getServer().broadcastMessage(message);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onChat(final AsyncPlayerChatEvent event) {
        if (!plugin.getConfig().getBoolean("share-chat.minecraft-to-twitch.enabled")) {
            return;
        }
        
        String message = plugin.getConfig().getString("share-chat.minecraft-to-twitch.format.unlinked");
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

        plugin.getLogger().info("Disconnected from Twitch.");
        
        AsyncPlayerChatEvent.getHandlerList().unregister(this);
    }
}

