package org.mateh.meowSMP.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.mateh.meowSMP.Main;

public class FallDamageListener implements Listener {

    private final Main main;

    public FallDamageListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        Player player = (Player) event.getEntity();
        if (hasTokenInHand(player, "jungle_cat") || hasTokenInHand(player, "house_cat")) {
            event.setCancelled(true);
        }
    }

    private boolean hasTokenInHand(Player player, String tokenKey) {
        if (player.getInventory().getItemInMainHand() != null &&
                player.getInventory().getItemInMainHand().hasItemMeta() &&
                player.getInventory().getItemInMainHand().getItemMeta().hasLore() &&
                player.getInventory().getItemInMainHand().getItemMeta().getLore().contains(tokenKey)) {
            return true;
        }
        if (player.getInventory().getItemInOffHand() != null &&
                player.getInventory().getItemInOffHand().hasItemMeta() &&
                player.getInventory().getItemInOffHand().getItemMeta().hasLore() &&
                player.getInventory().getItemInOffHand().getItemMeta().getLore().contains(tokenKey)) {
            return true;
        }
        return false;
    }
}
