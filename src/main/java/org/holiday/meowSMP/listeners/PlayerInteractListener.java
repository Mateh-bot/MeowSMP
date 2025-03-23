package org.holiday.meowSMP.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.holiday.meowSMP.data.PlayerData;
import org.holiday.meowSMP.enums.TokenType;
import org.holiday.meowSMP.managers.PlayerDataManager;
import org.holiday.meowSMP.utils.TokenUtils;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR))
            return;

        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        PlayerData data = PlayerDataManager.getPlayerData(player.getUniqueId());
        if (data == null) return;

        if (TokenUtils.isTokenItem(itemInHand)) {
            TokenType token = TokenUtils.getTokenTypeFromItem(itemInHand);
            if (token == null) return;

            if (token.isPrimary()) {
                if (data.getPrimaryToken() != null) {
                    if (data.getPrimaryToken() == token) {
                        player.sendMessage(ChatColor.RED + "You already have that primary token equipped.");
                        event.setCancelled(true);
                        return;
                    } else {
                        ItemStack oldToken = TokenUtils.createTokenItem(data.getPrimaryToken(), data.getPrimaryTokenLevel());
                        player.getInventory().addItem(oldToken);
                        player.sendMessage(ChatColor.GREEN + "Primary token changed to: " + ChatColor.YELLOW + token.getDisplayName());
                    }
                } else {
                    player.sendMessage(ChatColor.GREEN + "Primary token equipped: " + ChatColor.YELLOW + token.getDisplayName());
                }
                data.setPrimaryToken(token);
            } else {
                if (data.getSecondaryToken() != null) {
                    if (data.getSecondaryToken() == token) {
                        player.sendMessage(ChatColor.RED + "You already have that secondary token equipped.");
                        event.setCancelled(true);
                        return;
                    } else {
                        ItemStack oldToken = TokenUtils.createTokenItem(data.getSecondaryToken());
                        player.getInventory().addItem(oldToken);
                        player.sendMessage(ChatColor.GREEN + "Secondary token changed to: " + ChatColor.YELLOW + token.getDisplayName());
                    }
                } else {
                    player.sendMessage(ChatColor.GREEN + "Secondary token equipped: " + ChatColor.YELLOW + token.getDisplayName());
                }
                data.setSecondaryToken(token);
            }
            removeItemFromHand(player, itemInHand);
            event.setCancelled(true);
        } else {
            TokenType tokenToActivate = data.getPrimaryToken() != null ? data.getPrimaryToken() : data.getSecondaryToken();
            if (tokenToActivate == null) return;

            if (tokenToActivate == TokenType.CATFISH && !(player.getWorld().hasStorm() || player.isInWater())) {
                player.sendMessage(ChatColor.RED + "Catfish Token: The ability can only be activated when it is raining or you are in water.");
                event.setCancelled(true);
                return;
            }

            long baseCooldown = TokenUtils.getCooldown(tokenToActivate);
            long effectiveCooldown = baseCooldown;
            if (tokenToActivate.isPrimary()) {
                int level = data.getPrimaryTokenLevel();
                double reductionFactor = 1.0 - ((level - 1) * 0.1);
                effectiveCooldown = (long) (baseCooldown * reductionFactor);
            }

            Long lastUsed = data.getAbilityCooldown(tokenToActivate);
            long current = System.currentTimeMillis();
            if (lastUsed == null || current - lastUsed >= effectiveCooldown) {
                data.setAbilityCooldown(tokenToActivate, current);
                TokenUtils.triggerAbility(player, tokenToActivate);
            } else {
                long remainingSeconds = (effectiveCooldown - (current - lastUsed) + 999) / 1000;
            }
            event.setCancelled(true);
        }
    }

    private void removeItemFromHand(Player player, ItemStack item) {
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }
}
