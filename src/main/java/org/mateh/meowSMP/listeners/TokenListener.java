package org.mateh.meowSMP.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.tokens.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenListener implements Listener {

    private final Main main;
    private final Map<String, AbstractToken> tokenRegistry = new HashMap<>();
    private final Map<Player, Long> cooldowns = new HashMap<>();

    public TokenListener(Main main) {
        this.main = main;

        tokenRegistry.put("lion_token", new LionToken(main, main.getSQLiteManager()));
        tokenRegistry.put("cheetah_token", new CheetahToken(main, main.getSQLiteManager()));
        tokenRegistry.put("tiger_token", new TigerToken(main, main.getSQLiteManager()));
        tokenRegistry.put("house_cat", new HouseCatToken(main, main.getSQLiteManager()));
        tokenRegistry.put("catfish_token", new CatfishToken(main, main.getSQLiteManager()));
        tokenRegistry.put("village_cat", new VillageCatToken(main, main.getSQLiteManager()));
        tokenRegistry.put("black_cat", new BlackCatToken(main, main.getSQLiteManager()));
        tokenRegistry.put("jungle_cat", new JungleCatToken(main, main.getSQLiteManager()));
        tokenRegistry.put("sphinx_token", new SphinxToken(main, main.getSQLiteManager()));

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : main.getServer().getOnlinePlayers()) {
                    applyPassiveFromItem(player, player.getInventory().getItemInMainHand());
                    applyPassiveFromItem(player, player.getInventory().getItemInOffHand());
                }
            }
        }.runTaskTimer(main, 0L, 60L);
    }

    private void applyPassiveFromItem(Player player, ItemStack item) {
        if (item == null || !item.hasItemMeta())
            return;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore())
            return;
        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty())
            return;
        String tokenKey = lore.get(0);
        AbstractToken token = tokenRegistry.get(tokenKey);
        if (token != null) {
            token.getPassiveEffects().forEach(effect -> {
                if (player.hasPotionEffect(effect.getType())) {
                    if (player.getPotionEffect(effect.getType()).getAmplifier() > effect.getAmplifier()) {
                        return;
                    }
                }
                player.removePotionEffect(effect.getType());
                player.addPotionEffect(new PotionEffect(
                        effect.getType(),
                        16 * 20,
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.hasParticles(),
                        effect.hasIcon()
                ));
            });
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore())
            return;
        List<String> lore = item.getItemMeta().getLore();
        if (lore == null || lore.isEmpty())
            return;
        String tokenKey = lore.get(0);
        AbstractToken token = tokenRegistry.get(tokenKey);
        if (token == null)
            return;
        if (!token.isValidAction(event))
            return;
        long now = System.currentTimeMillis();
        if (cooldowns.containsKey(player) && now < cooldowns.get(player)) {
            player.sendMessage(ChatColor.RED + "Ability is on cooldown.");
            return;
        }
        cooldowns.put(player, now + token.getCooldown() * 1000L);
        token.showCooldownBossBar(player);
        token.activateAbility(player);
    }
}
