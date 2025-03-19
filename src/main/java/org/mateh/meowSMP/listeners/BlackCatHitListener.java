package org.mateh.meowSMP.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.tokens.BlackCatToken;

import java.util.List;
import java.util.Random;

public class BlackCatHitListener implements Listener {

    private final Main main;
    private final Random random = new Random();

    public BlackCatHitListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player damager = (Player) event.getDamager();
        if (!(event.getEntity() instanceof LivingEntity)) return;

        if (!BlackCatToken.activePlayers.containsKey(damager.getUniqueId().toString()) ||
                !BlackCatToken.activePlayers.get(damager.getUniqueId().toString())) {
            return;
        }

        if (random.nextDouble() <= 0.20) {
            LivingEntity target = (LivingEntity) event.getEntity();
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1200, 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 1));

            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1.0f, 1.0f);
            target.getWorld().spawnParticle(Particle.WITCH, target.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.1);

            damager.sendMessage(ChatColor.DARK_PURPLE + "Black Cat effect triggered on hit!");
            BlackCatToken.activePlayers.remove(damager.getUniqueId().toString());
        }
    }

    private boolean hasTokenEquipped(Player player, String tokenKey) {
        return hasTokenInItem(player.getInventory().getItemInMainHand(), tokenKey) ||
                hasTokenInItem(player.getInventory().getItemInOffHand(), tokenKey);
    }

    private boolean hasTokenInItem(ItemStack item, String tokenKey) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return false;
        List<String> lore = meta.getLore();
        return lore != null && !lore.isEmpty() && lore.get(0).equalsIgnoreCase(tokenKey);
    }
}
