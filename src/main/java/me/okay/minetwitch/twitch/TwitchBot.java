package me.okay.minetwitch.twitch;

import com.github.twitch4j.TwitchClient;

public class TwitchBot {
    private TwitchClient twitchClient;
    private String channelName;
    
    public TwitchBot(TwitchClient twitchClient, String channelName) {
        this.twitchClient = twitchClient;
        this.channelName = channelName;
    }
    
    public void sendMessage(String message) {
        twitchClient.getChat().sendMessage(channelName, message);
    }
}
