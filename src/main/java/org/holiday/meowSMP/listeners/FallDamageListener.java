package org.holiday.meowSMP.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.holiday.meowSMP.data.PlayerData;
import org.holiday.meowSMP.enums.TokenType;
import org.holiday.meowSMP.managers.PlayerDataManager;

public class FallDamageListener implements Listener {
    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        Player player = (Player) event.getEntity();
        PlayerData data = PlayerDataManager.getPlayerData(player.getUniqueId());
        if (data == null) return;

        if ((data.getPrimaryToken() == TokenType.HOUSE_CAT || data.getSecondaryToken() == TokenType.HOUSE_CAT) ||
                (data.getPrimaryToken() == TokenType.JUNGLE_CAT || data.getSecondaryToken() == TokenType.JUNGLE_CAT)) {
            event.setCancelled(true);
        }
    }
}
