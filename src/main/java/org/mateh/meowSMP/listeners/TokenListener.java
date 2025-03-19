package org.mateh.meowSMP.listeners;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.CooldownData;
import org.mateh.meowSMP.tokens.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenListener implements Listener {
    private final Main main;
    private final Map<String, AbstractToken> tokenRegistry = new HashMap<>();
    private final Map<Player, AbstractToken> activeTokens = new HashMap<>();
    private final Map<Player, Long> cooldowns = new HashMap<>();
    private final Map<Player, CooldownData> cooldownTasks = new HashMap<>();
    private final Map<Player, Boolean> isActivating = new HashMap<>();

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
                    checkAndApplyHealthBonus(player);
                    AbstractToken active = activeTokens.get(player);
                    if (active != null) {
                        active.getPassiveEffects().forEach(effect -> {
                            if (player.hasPotionEffect(effect.getType())) {
                                if (player.getPotionEffect(effect.getType()).getAmplifier() > effect.getAmplifier())
                                    return;
                            }
                            player.removePotionEffect(effect.getType());
                            player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                                    effect.getType(),
                                    16 * 20, // 16 seconds
                                    effect.getAmplifier(),
                                    effect.isAmbient(),
                                    effect.hasParticles(),
                                    effect.hasIcon()
                            ));
                        });
                    }
                }
            }
        }.runTaskTimer(main, 0L, 60L);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.getAction().toString().startsWith("RIGHT_CLICK"))
            return;

        long now = System.currentTimeMillis();
        if (cooldowns.containsKey(player) && now >= cooldowns.get(player)) {
            resetCooldown(player);
        }

        if (cooldownTasks.containsKey(player)) {
            player.sendMessage(ChatColor.RED + "Ability is on cooldown.");
            return;
        }

        if (activeTokens.containsKey(player)) {
            AbstractToken token = activeTokens.get(player);
            if (!token.isValidAction(event))
                return;
            if (cooldowns.containsKey(player) && now < cooldowns.get(player)) {
                player.sendMessage(ChatColor.RED + "Ability is on cooldown.");
                return;
            }
            int effectiveCooldown = token.getCooldown();
            long newCooldownEnd = now + effectiveCooldown * 1000L;
            cooldowns.put(player, newCooldownEnd);
            CooldownData cd = token.createCooldownData(player, effectiveCooldown);
            cd.getTask().runTaskTimer(main, 0L, 20L);
            cooldownTasks.put(player, cd);
            main.getSQLiteManager().setActiveToken(player.getUniqueId().toString(), token.getKey(), 1, 0, newCooldownEnd);
            token.activateAbility(player);
            main.getServer().getScheduler().runTaskLater(main, () -> {
                cooldowns.remove(player);
                cooldownTasks.remove(player);
            }, effectiveCooldown * 20L);
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore())
            return;
        List<String> lore = item.getItemMeta().getLore();
        if (lore.isEmpty())
            return;
        String tokenKey = lore.get(0);
        AbstractToken token = tokenRegistry.get(tokenKey);
        if (token == null)
            return;
        equipToken(player, token, item);
        event.setCancelled(true);
    }

    private void checkAndApplyHealthBonus(Player player) {
        double bonus = 0.0;
        AbstractToken active = activeTokens.get(player);
        if (active != null) {
            String key = active.getKey();
            if (key.equals("tiger_token"))
                bonus = 10.0;
            else if (key.equals("sphinx_token"))
                bonus = 20.0;
        }
        double newMaxHealth = 20.0 + bonus;
        if (player.getAttribute(Attribute.MAX_HEALTH).getBaseValue() != newMaxHealth) {
            player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(newMaxHealth);
        }
    }

    private void equipToken(Player player, AbstractToken token, ItemStack tokenItem) {
        int amount = tokenItem.getAmount();
        if (amount > 1) {
            tokenItem.setAmount(amount - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
        player.updateInventory();
        activeTokens.put(player, token);
        long cooldownEnd = System.currentTimeMillis();
        main.getSQLiteManager().setActiveToken(player.getUniqueId().toString(), token.getKey(), 1, 0, cooldownEnd);
        player.sendMessage(ChatColor.GREEN + token.getDisplayName() + " equipped! Now you can activate its ability by right-clicking anywhere.");
    }

    public void setActiveToken(Player player, AbstractToken token) {
        activeTokens.put(player, token);
        main.getSQLiteManager().setActiveToken(player.getUniqueId().toString(), token.getKey(), 1, 0, System.currentTimeMillis());
    }

    public void resetCooldown(Player player) {
        if (cooldownTasks.containsKey(player)) {
            CooldownData cd = cooldownTasks.remove(player);
            cd.getTask().cancel();
            cd.getBossBar().removeAll();
        }
        cooldowns.remove(player);
    }

    public void removeActiveToken(Player player) {
        resetCooldown(player);
        activeTokens.remove(player);
        main.getSQLiteManager().removeActiveToken(player.getUniqueId().toString());
        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20.0);
    }

    public Map<String, AbstractToken> getTokenRegistry() {
        return tokenRegistry;
    }

    public AbstractToken getActiveToken(Player player) {
        return activeTokens.get(player);
    }

    public void setCooldown(Player player, long cooldownEnd) {
        cooldowns.put(player, cooldownEnd);
    }

    public void addCooldownTask(Player player, CooldownData cd) {
        cooldownTasks.put(player, cd);
    }
}
