package me.okay.minetwitch.data.database;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

public interface Database {
    public void safeDisconnect();

    public boolean isClosed();

    public void setLinkedAccount(UUID minecraftUuid, String twitchId);

    public void removeLinkedAccount(UUID minecraftUuid);
    public void removeLinkedAccount(String twitchId);

    public default String getTwitchId(OfflinePlayer player) {
        return getTwitchId(player.getUniqueId());
    }
    public String getTwitchId(UUID minecraftUuid);
    
    public UUID getMinecraftUuid(String twitchId);
}
