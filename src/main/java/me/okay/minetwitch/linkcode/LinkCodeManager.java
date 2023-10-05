package me.okay.minetwitch.linkcode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import me.okay.minetwitch.MinetwitchPlugin;

public class LinkCodeManager {
    private MinetwitchPlugin plugin;

    private Map<UUID, LinkCode> linkCodes = new HashMap<>();
    private Map<UUID, BukkitTask> linkExpireTasks = new HashMap<>();

    public LinkCodeManager(MinetwitchPlugin plugin) {
        this.plugin = plugin;
    }

    public void setLinkCode(UUID uuid, LinkCode linkCode) {
        linkCodes.put(uuid, linkCode);

        BukkitTask bukkitTask = linkExpireTasks.get(uuid);
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }

        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            linkCodes.remove(uuid);
            linkExpireTasks.remove(uuid);
        }, linkCode.getExpiresIn() * 20);

        linkExpireTasks.put(uuid, task);
    }

    public UUID getUUIDFromLinkCode(String linkCode) {
        for (Map.Entry<UUID, LinkCode> entry : linkCodes.entrySet()) {
            if (entry.getValue().getCode().equals(linkCode)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public void removeCodes(UUID minecraftUuid, String twitchId) {
        linkCodes.remove(minecraftUuid);
        linkExpireTasks.get(minecraftUuid).cancel();
        linkExpireTasks.remove(minecraftUuid);
    }
}
