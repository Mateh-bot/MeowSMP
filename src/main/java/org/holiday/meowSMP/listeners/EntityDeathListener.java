package org.holiday.meowSMP.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.holiday.meowSMP.data.PlayerData;
import org.holiday.meowSMP.managers.PlayerDataManager;

public class EntityDeathListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity().getKiller() instanceof Player) {
            Player killer = event.getEntity().getKiller();
            PlayerData data = PlayerDataManager.getPlayerData(killer.getUniqueId());
            if (data != null && data.getPrimaryToken() != null) {
                data.incrementKillCount();
                if (data.getKillCount() % 5 == 0) {
                    if (data.getPrimaryTokenLevel() < 4) {
                        data.incrementPrimaryTokenLevel();
                        killer.sendMessage(ChatColor.GREEN + "Your primary token goes up to level " + data.getPrimaryTokenLevel() + "!");
                        killer.getWorld().playSound(killer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    } else {
                        killer.sendMessage(ChatColor.YELLOW + "Your primary token has reached the maximum level!");
                    }
                }
            }
        }
    }
}
