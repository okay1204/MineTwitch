package me.okay.minetwitch.data;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import me.okay.minetwitch.MinetwitchPlugin;

public class LinkedAccounts {
    private final Map<UUID, Optional<Integer>> mcKeyMap = new ConcurrentHashMap<>();
    private final Map<Integer, Optional<UUID>> twitchKeyMap = new ConcurrentHashMap<>();

    private MinetwitchPlugin plugin;
    private BukkitTask autoFlushTask;

    public LinkedAccounts(MinetwitchPlugin plugin) {
        this.plugin = plugin;

        // Run every 5 minutes
        autoFlushTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveCache, 6000, 6000);
    }

    public void stopAutoFlush() {
        autoFlushTask.cancel();
    }

    public void saveCache() {
        Iterator<Map.Entry<UUID, Optional<Integer>>> mcKeyMapIterator = mcKeyMap.entrySet().iterator();
        while (mcKeyMapIterator.hasNext()) {
            Map.Entry<UUID, Optional<Integer>> entry = mcKeyMapIterator.next();

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

        Iterator<Map.Entry<Integer, Optional<UUID>>> twitchKeyMapIterator = twitchKeyMap.entrySet().iterator();
        while (twitchKeyMapIterator.hasNext()) {
            Map.Entry<Integer, Optional<UUID>> entry = twitchKeyMapIterator.next();

            if (!entry.getValue().isPresent()) {
                plugin.getDatabase().removeLinkedAccount(entry.getKey());
            }

            if (entry.getValue().isPresent() && !Bukkit.getPlayer(entry.getValue().get()).isOnline()) {
                twitchKeyMapIterator.remove();
            }
        }
    }
    
    @Nullable
    public Optional<Integer> getTwitchId(UUID minecraftUuid) {
        Optional<Integer> result = mcKeyMap.get(minecraftUuid);

        if (result == null) {
            result = plugin.getDatabase().getTwitchId(minecraftUuid);
            mcKeyMap.put(minecraftUuid, result);
        }

        return result;
    }
    
    @Nullable
    public Optional<Integer> getTwitchId(OfflinePlayer player) {
        return getTwitchId(player.getUniqueId());
    }
    
    @Nullable
    public Optional<UUID> getMinecraftUuid(int twitchId) {
        Optional<UUID> result = twitchKeyMap.get(twitchId);

        if (result == null) {
            result = plugin.getDatabase().getMinecraftUuid(twitchId);
            twitchKeyMap.put(twitchId, result);
        }

        return result;
    }

    public void setLinkedAccounts(UUID minecraftUuid, Integer twitchId) {

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
        Optional<Integer> twitchId = mcKeyMap.get(minecraftUuid);

        if (twitchId != null && twitchId.isPresent()) {
            twitchKeyMap.put(twitchId.get(), Optional.empty());
        }
    }

    public void remove(Integer twitchId) {
        Optional<UUID> minecraftUuid = twitchKeyMap.remove(twitchId);

        if (minecraftUuid != null && minecraftUuid.isPresent()) {
            mcKeyMap.put(minecraftUuid.get(), Optional.empty());
        }
    }
}
