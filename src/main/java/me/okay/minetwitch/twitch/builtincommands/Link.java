package me.okay.minetwitch.twitch.builtincommands;

import java.util.UUID;

import org.bukkit.Bukkit;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import me.okay.minetwitch.MinetwitchPlugin;
import me.okay.minetwitch.twitch.TwitchBot;
import me.okay.minetwitch.twitch.TwitchCommand;
import me.okay.minetwitch.twitch.TwitchCommandExecutor;

public class Link implements TwitchCommandExecutor {
    private MinetwitchPlugin plugin;

    public Link(MinetwitchPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(TwitchBot twitchBot, ChannelMessageEvent messageEvent, String[] args, String label, TwitchCommand command) {
        if (args.length == 0) {
            twitchBot.sendMessage("To link your Twitch account to your Minecraft account, log on to the Minecraft server and run \"/minetwitch link\". Then copy the code from the server and paste it here in this format: /link <code>");
            return;
        }

        String code = args[0];
        UUID minecraftUuid = plugin.getLinkCodeManager().getUUIDFromLinkCode(code);
        if (minecraftUuid == null) {
            twitchBot.sendMessage("Invalid code! Please double check your code.");
            return;
        }
        int twitchId = Integer.parseInt(messageEvent.getUser().getId());

        plugin.getLinkCodeManager().removeCodes(minecraftUuid);
        
        if (plugin.getLinkedAccountsManager().getMinecraftUuid(twitchId).isPresent()) {
            twitchBot.sendMessage("You are already linked to a Minecraft account! Please unlink your current account first using " + plugin.getCommandPrefix() + "unlink."); // TODO add !unlink command
            return;
        }
        else if (plugin.getLinkedAccountsManager().getTwitchId(minecraftUuid).isPresent()) {
            twitchBot.sendMessage("This Minecraft account is already linked to a Twitch account! Please unlink your current account first using \"/minetwitch unlink\" in game."); // TODO add /minetwitch unlink command
            return;
        }
        
        plugin.getLinkedAccountsManager().setLinkedAccounts(minecraftUuid, twitchId);
        twitchBot.sendMessage("Your Twitch account has been sucessfully linked to your Minecraft account: " + Bukkit.getOfflinePlayer(minecraftUuid).getName());
    }
}
