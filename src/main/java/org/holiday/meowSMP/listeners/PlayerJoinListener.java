package org.holiday.meowSMP.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.holiday.meowSMP.Main;
import org.holiday.meowSMP.data.PlayerData;
import org.holiday.meowSMP.enums.TokenType;
import org.holiday.meowSMP.managers.DataManager;
import org.holiday.meowSMP.managers.PlayerDataManager;
import org.holiday.meowSMP.utils.TokenUtils;

import java.util.Random;
import java.util.UUID;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerData data = PlayerDataManager.getPlayerData(uuid);
        if (data == null) {
            DataManager dm = Main.getInstance().getDataManager();
            data = dm.getPlayerData(uuid);
            if (data != null) {
                PlayerDataManager.addPlayerData(uuid, data);
            }
        }
        if (data == null) {
            data = new PlayerData();
            PlayerDataManager.addPlayerData(uuid, data);
        }

        if (!data.hasReceivedInitialToken()) {
            TokenType[] secondaryTokens = {TokenType.CATFISH, TokenType.VILLAGE_CAT, TokenType.BLACK_CAT, TokenType.JUNGLE_CAT};
            TokenType randomToken = secondaryTokens[new Random().nextInt(secondaryTokens.length)];
            ItemStack tokenItem = TokenUtils.createTokenItem(randomToken);
            player.getInventory().addItem(tokenItem);
            player.sendMessage(ChatColor.GREEN + "You have been given a token: " + ChatColor.YELLOW + randomToken.getDisplayName() + ". " + ChatColor.GREEN + "Right-click to equip it.");
            data.setHasReceivedInitialToken(true);
            Main.getInstance().getDataManager().savePlayer(uuid);
        }

        if (data.getPrimaryToken() != null) {
            TokenUtils.applyTokenEffects(player, data.getPrimaryToken());
        }
        if (data.getSecondaryToken() != null) {
            TokenUtils.applyTokenEffects(player, data.getSecondaryToken());
        }
    }
}
