package me.okay.minetwitch.data;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.okay.minetwitch.MinetwitchPlugin;

public class LinkedAccounts {
    private final Map<UUID, Optional<String>> mcKeyMap = new ConcurrentHashMap<>();
    private final Map<String, Optional<UUID>> twitchKeyMap = new ConcurrentHashMap<>();

    private MinetwitchPlugin plugin;

    public LinkedAccounts(MinetwitchPlugin plugin) {
        this.plugin = plugin;

        // Run every 5 minutes
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveCache, 6000, 6000);
    }

    public void saveCache() {
        Iterator<Map.Entry<UUID, Optional<String>>> mcKeyMapIterator = mcKeyMap.entrySet().iterator();
        while (mcKeyMapIterator.hasNext()) {
            Map.Entry<UUID, Optional<String>> entry = mcKeyMapIterator.next();

            if (entry.getValue().isPresent()) {
                plugin.getDatabase().setLinkedAccount(entry.getKey(), entry.getValue().get());
            }
            else {
                plugin.getDatabase().removeLinkedAccount(entry.getKey());
            }

            if (!Bukkit.getPlayer(entry.getKey()).isOnline()) {
                mcKeyMapIterator.remove();
            }
        }

        Iterator<Map.Entry<String, Optional<UUID>>> twitchKeyMapIterator = twitchKeyMap.entrySet().iterator();
        while (twitchKeyMapIterator.hasNext()) {
            Map.Entry<String, Optional<UUID>> entry = twitchKeyMapIterator.next();

            if (!entry.getValue().isPresent()) {
                plugin.getDatabase().removeLinkedAccount(entry.getKey());
            }

            if (entry.getValue().isPresent() && !Bukkit.getPlayer(entry.getValue().get()).isOnline()) {
                twitchKeyMapIterator.remove();
            }
        }
    }
    
    public String getTwitchId(UUID minecraftUuid) {
        Optional<String> result = mcKeyMap.get(minecraftUuid);

        if (result == null) {
            result = Optional.ofNullable(plugin.getDatabase().getTwitchId(minecraftUuid));
            mcKeyMap.put(minecraftUuid, result);
        }

        return result.orElse(null);
    }
    
    public String getTwitchId(OfflinePlayer player) {
        return getTwitchId(player.getUniqueId());
    }
    
    public UUID getMinecraftUuid(String twitchId) {
        Optional<UUID> result = twitchKeyMap.get(twitchId);

        if (result == null) {
            result = Optional.ofNullable(plugin.getDatabase().getMinecraftUuid(twitchId));
            twitchKeyMap.put(twitchId, result);
        }

        return result.orElse(null);
    }

    public void setLinkedAccounts(UUID minecraftUuid, String twitchId) {

        if (minecraftUuid != null) {
            remove(minecraftUuid);
            mcKeyMap.put(minecraftUuid, Optional.ofNullable(twitchId));
        }

        if (twitchId != null) {
            remove(twitchId);
            twitchKeyMap.put(twitchId, Optional.ofNullable(minecraftUuid));
        }

    }

    public void remove(UUID minecraftUuid) {
        Optional<String> twitchId = mcKeyMap.get(minecraftUuid);

        if (twitchId != null && twitchId.isPresent()) {
            twitchKeyMap.put(twitchId.get(), Optional.empty());
        }
    }

    public void remove(String twitchId) {
        Optional<UUID> minecraftUuid = twitchKeyMap.remove(twitchId);

        if (minecraftUuid != null && minecraftUuid.isPresent()) {
            mcKeyMap.put(minecraftUuid.get(), Optional.empty());
        }
    }
}
