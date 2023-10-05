package me.okay.minetwitch.data.database;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

public interface Database {
    public void safeDisconnect();

    public boolean isClosed();

    public void setLinkedAccount(UUID minecraftUuid, int twitchId);

    public void removeLinkedAccount(UUID minecraftUuid);
    public void removeLinkedAccount(int twitchId);

    public default Optional<Integer> getTwitchId(OfflinePlayer player) {
        return getTwitchId(player.getUniqueId());
    }
    public Optional<Integer> getTwitchId(UUID minecraftUuid);
    
    public Optional<UUID> getMinecraftUuid(int twitchId);
}
