package org.holiday.meowSMP.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.holiday.meowSMP.data.PlayerData;
import org.holiday.meowSMP.enums.TokenType;
import org.holiday.meowSMP.managers.PlayerDataManager;

import java.util.Random;

public class PlayerDamageListener implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player target = (Player) event.getEntity();
            PlayerData data = PlayerDataManager.getPlayerData(damager.getUniqueId());
            if (data != null && data.getSecondaryToken() == TokenType.BLACK_CAT) {
                if (new Random().nextDouble() <= 0.2) {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60 * 20, 1));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60 * 20, 0));
                    damager.sendMessage(ChatColor.DARK_PURPLE + "Black Cat activated in " + target.getName() + "!");
                }
            }
        }
    }
}
