package org.mateh.meowSMP.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.PlayerTokenData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KillListener implements Listener {
    private final Main main;
    private final Map<UUID, PlayerTokenData> tokenDataMap = new HashMap<>();

    public KillListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            UUID uuid = killer.getUniqueId();
            PlayerTokenData data = tokenDataMap.getOrDefault(uuid, new PlayerTokenData(30.0));
            data.incrementKillCount();
            if (data.getKillCount() % 5 == 0) {
                data.levelUp();
                killer.sendMessage(ChatColor.GREEN + "Your token has leveled up! New cooldown: " + data.getCurrentCooldown() + " seconds.");
            }
            tokenDataMap.put(uuid, data);
        }
    }

    public PlayerTokenData getTokenData(UUID uuid) {
        return tokenDataMap.get(uuid);
    }
}
