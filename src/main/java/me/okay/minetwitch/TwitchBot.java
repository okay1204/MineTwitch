package me.okay.minetwitch;

import java.util.Arrays;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.helix.domain.UserList;

public class TwitchBot {
    private Minetwitch plugin;
    private String channelName;
    private TwitchClient twitchClient;

    public TwitchBot(Minetwitch plugin) {
        this.plugin = plugin;

        // // chat credentials
        // OAuth2Credential credentials = new OAuth2Credential("twitch", plugin.getConfig().getString("twitch-info.token", ""));

        // TwitchIdentityProvider identityProvider = new TwitchIdentityProvider(null, null, null);

        // if (!identityProvider.isCredentialValid(credentials).get()) {
        //     plugin.getLogger().warning("Invalid Twitch token! Please check your config.yml and reload.");
        //     channelName = null;
        //     return;
        // }
    
        // // twitch client
        // twitchClient = TwitchClientBuilder.builder()
        //     .withEnableChat(true)
        //     .withChatAccount(credentials)
        //     .withEnableHelix(true)
        //     .build();
    
            
        // channelName = plugin.getConfig().getString("twitch-info.channel-name", "");
        // if (channelName.length() == 0) {
        //     channelName = identityProvider.getAdditionalCredentialInformation(credentials).map(OAuth2Credential::getUserName).orElse(null);
        // }
        // else {
        //     UserList resultList = twitchClient.getHelix().getUsers(credentials.getAccessToken(), null, Arrays.asList(channelName)).execute();
        //     if (resultList.getUsers().size() == 0) {
        //         plugin.getLogger().warning("Invalid Twitch channel name! Please check your config.yml and reload.");
        //         return;
        //     }
        // }
        
        // plugin.getLogger().info("Connecting to Twitch channel: " + channelName);
        // twitchClient.getChat().joinChannel(channelName);
    
        // twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, event -> {
        //     System.out.println("[" + event.getChannel().getName() + "] " + event.getUser().getName() + ": " + event.getMessage());
        //     twitchClient.getChat().sendMessage(channelName, "dumb");
        // });

    }

    public void disconnect() {
        plugin.getLogger().info("Disconnecting from Twitch...");

        // if (twitchClient != null) {
        //     twitchClient.getChat().leaveChannel(channelName);
        //     twitchClient.close();
        // }

        plugin.getLogger().info("Disconnected from Twitch.");
    }
}

