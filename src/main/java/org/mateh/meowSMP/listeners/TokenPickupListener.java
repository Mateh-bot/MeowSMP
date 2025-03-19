package org.mateh.meowSMP.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class TokenPickupListener implements Listener {

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();
        if (item.getType() == Material.PAPER && item.hasItemMeta() && item.getItemMeta().hasLore()) {
            if (playerHasToken(player)) {
                player.sendMessage(ChatColor.RED + "You already have a token!");
                event.setCancelled(true);
            }
        }
    }

    private boolean playerHasToken(Player player) {
        return player.getInventory().all(Material.PAPER).values().stream().anyMatch(item -> {
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                String tokenKey = item.getItemMeta().getLore().get(0);
                return tokenKey.equals("lion_token") || tokenKey.equals("cheetah_token") ||
                        tokenKey.equals("tiger_token") || tokenKey.equals("house_cat") ||
                        tokenKey.equals("catfish_token") || tokenKey.equals("village_cat") ||
                        tokenKey.equals("black_cat") || tokenKey.equals("jungle_cat") ||
                        tokenKey.equals("sphinx_token");
            }
            return false;
        });
    }
}
