package org.holiday.meowSMP.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.holiday.meowSMP.data.PlayerData;
import org.holiday.meowSMP.managers.PlayerDataManager;
import org.holiday.meowSMP.utils.TokenUtils;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerData data = PlayerDataManager.getPlayerData(player.getUniqueId());
        if (data == null) return;
        if (data.getPrimaryToken() != null) {
            ItemStack tokenItem = TokenUtils.createTokenItem(data.getPrimaryToken(), data.getPrimaryTokenLevel());
            player.getWorld().dropItemNaturally(player.getLocation(), tokenItem);
            data.setPrimaryToken(null);
        }
        if (data.getSecondaryToken() != null) {
            ItemStack tokenItem = TokenUtils.createTokenItem(data.getSecondaryToken());
            player.getWorld().dropItemNaturally(player.getLocation(), tokenItem);
            data.setSecondaryToken(null);
        }
    }
}
