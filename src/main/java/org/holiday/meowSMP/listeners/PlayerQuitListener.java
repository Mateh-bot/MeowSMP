package org.holiday.meowSMP.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.holiday.meowSMP.data.PlayerData;
import org.holiday.meowSMP.managers.DataManager;
import org.holiday.meowSMP.managers.PlayerDataManager;

public class PlayerQuitListener implements Listener {
    private final DataManager dataManager;

    public PlayerQuitListener(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerData data = PlayerDataManager.getPlayerData(event.getPlayer().getUniqueId());
        if (data != null) {
            dataManager.addPlayerData(event.getPlayer().getUniqueId(), data);
            dataManager.savePlayer(event.getPlayer().getUniqueId());
        }
    }
}
