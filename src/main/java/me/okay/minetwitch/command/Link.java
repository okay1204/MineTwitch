package me.okay.minetwitch.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.okay.minetwitch.MinetwitchPlugin;
import me.okay.minetwitch.customcommand.CommandResult;
import me.okay.minetwitch.customcommand.CustomSubcommand;
import me.okay.minetwitch.linkcode.LinkCode;
import me.okay.minetwitch.utils.TextFormat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Link extends CustomSubcommand {
    private static final int EXPIRE_TIME = 120;

    private MinetwitchPlugin plugin;

    public Link(MinetwitchPlugin plugin) {
        super(
            "link",
            "Links your Twitch account to your Minecraft account.",
            "minetwitch.command.link"
        );

        this.plugin = plugin;
    }
    
    @Override
    public CommandResult run(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.colorize("&cYou must be a player to use this command."));
            return CommandResult.FINISHED;
        }

        Player player = (Player) sender;

        // Check if the player is already linked
        if (plugin.getLinkedAccountsManager().getTwitchId(player.getUniqueId()).isPresent()) {
            sender.sendMessage(TextFormat.colorize("&cYou are already linked to a Twitch account! Please unlink your current account first using &4/minetwitch unlink&c."));
            return CommandResult.FINISHED;
        }

        // Generate a random code 6 character code with letters and numbers
        String code = "";
        for (int i = 0; i < 6; i++) {
            int random = (int) (Math.random() * 62);
            if (random < 10) {
                code += random;
            } else if (random < 36) {
                code += (char) ('a' + random - 10);
            } else {
                code += (char) ('A' + random - 36);
            }
        }

        // Create a new link code
        LinkCode linkCode = new LinkCode(code, EXPIRE_TIME);

        String twitchCommand = plugin.getCommandPrefix() + "link " + code;

        // Send the link code to the player
        TextComponent textComponent = new TextComponent(
        TextFormat.colorize("&6Your link code is &a" + code + "&6. Use &a" + twitchCommand +
        " &6in Twitch chat to link your account. This code will expire in " + EXPIRE_TIME + " seconds. ")
        );
        
        TextComponent clickComponent = new TextComponent(TextFormat.colorize("&a&l[COPY]"));
        clickComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, twitchCommand));
        clickComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(TextFormat.colorize("&aClick to copy to clipboard."))));
        
        player.spigot().sendMessage(textComponent, clickComponent);

        // Add the link code to the map
        plugin.getLinkCodeManager().setLinkCode(player.getUniqueId(), linkCode);
            
        return CommandResult.FINISHED;
    }
}
