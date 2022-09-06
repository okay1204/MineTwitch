package me.okay.minetwitch.twitch;

public interface TwitchCommandExecutor {
    public void execute(TwitchBot twitchBot, String label, String[] args, TwitchCommand command);
}
