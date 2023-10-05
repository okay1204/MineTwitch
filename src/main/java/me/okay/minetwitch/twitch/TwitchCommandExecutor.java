package me.okay.minetwitch.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public interface TwitchCommandExecutor {
    public void execute(TwitchBot twitchBot, ChannelMessageEvent messageEvent, String[] args, String label, TwitchCommand command);
}
