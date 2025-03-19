package org.mateh.meowSMP.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.TokenItems;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.CooldownData;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TokenSpawnListener implements Listener {
    private final Main main;
    private final TokenListener tokenListener;
    private final Random random = new Random();
    private final List<String> secondaryTokenKeys = Arrays.asList("catfish_token", "village_cat", "black_cat", "jungle_cat");

    public TokenSpawnListener(Main main, TokenListener tokenListener) {
        this.main = main;
        this.tokenListener = tokenListener;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        var activeData = main.getSQLiteManager().loadActiveToken(player.getUniqueId().toString());
        if (activeData != null) {
            AbstractToken token = tokenListener.getTokenRegistry().get(activeData.getTokenKey());
            if (token != null) {
                tokenListener.setActiveToken(player, token);
                long remaining = activeData.getCooldownEnd() - System.currentTimeMillis();
                if (remaining > 0) {
                    int remainingSeconds = (int) Math.ceil(remaining / 1000.0);
                    var cd = token.createCooldownData(player, remainingSeconds);
                    cd.getTask().runTaskTimer(main, 0L, 20L);
                    tokenListener.setCooldown(player, activeData.getCooldownEnd());
                    tokenListener.addCooldownTask(player, cd);
                } else {
                    tokenListener.resetCooldown(player);
                }
                player.sendMessage("Your active token (" + token.getDisplayName() + ") has been restored.");
                return;
            }
        }
        if (!playerHasToken(player)) {
            giveRandomToken(player);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        main.getServer().getScheduler().runTaskLater(main, () -> {
            if (!playerHasToken(player)) {
                giveRandomToken(player);
            }
        }, 1L);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(item -> isToken(item));
        Player player = event.getEntity();
        tokenListener.removeActiveToken(player);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (isToken(item)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot drop your token.");
        }
    }

    private boolean playerHasToken(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isToken(item))
                return true;
        }
        return false;
    }

    private boolean isToken(ItemStack item) {
        if (item.getType() != Material.PAPER) return false;
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return false;
        String tokenKey = item.getItemMeta().getLore().get(0);
        return secondaryTokenKeys.contains(tokenKey);
    }

    private void giveRandomToken(Player player) {
        int index = random.nextInt(secondaryTokenKeys.size());
        String tokenKey = secondaryTokenKeys.get(index);
        ItemStack tokenItem;
        switch (tokenKey) {
            case "catfish_token":
                tokenItem = TokenItems.createCatfishTokenItem(main);
                break;
            case "village_cat":
                tokenItem = TokenItems.createVillageCatTokenItem(main);
                break;
            case "black_cat":
                tokenItem = TokenItems.createBlackCatTokenItem(main);
                break;
            case "jungle_cat":
                tokenItem = TokenItems.createJungleCatTokenItem(main);
                break;
            default:
                tokenItem = TokenItems.createCatfishTokenItem(main);
                break;
        }
        player.getInventory().addItem(tokenItem);
        player.sendMessage(ChatColor.GREEN + "You have received a secondary token: " + tokenKey);
    }
}
