package org.mateh.meowSMP.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.TokenItems;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TokenSpawnListener implements Listener {

    private final Main main;
    private final Random random = new Random();
    // Lista de tokens secundarios
    private final List<String> secondaryTokenKeys = Arrays.asList("catfish_token", "village_cat", "black_cat", "jungle_cat");

    public TokenSpawnListener(Main main) {
        this.main = main;
    }

    // Cuando el jugador se une al servidor, si no tiene un token, se le da uno aleatorio.
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!playerHasToken(player)) {
            giveRandomToken(player);
        }
    }

    // Cuando el jugador respawnea, se le asigna nuevamente un token si no lo tiene.
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        // Se programa un retraso corto para que el inventario esté disponible
        main.getServer().getScheduler().runTaskLater(main, () -> {
            if (!playerHasToken(player)) {
                giveRandomToken(player);
            }
        }, 1L);
    }

    // Al morir, se eliminan los tokens de los drops para que no se puedan recoger.
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(item -> isToken(item));
    }

    // Comprueba si el jugador ya tiene un token (en este ejemplo, se asume que son items PAPER con lore)
    private boolean playerHasToken(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isToken(item)) {
                return true;
            }
        }
        return false;
    }

    // Determina si el item es un token comprobando que sea PAPER y su lore coincida con uno de los token keys secundarios.
    private boolean isToken(ItemStack item) {
        if (item.getType() != Material.PAPER) return false;
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return false;
        String tokenKey = item.getItemMeta().getLore().get(0);
        return secondaryTokenKeys.contains(tokenKey);
    }

    // Da al jugador un token aleatorio entre los tokens secundarios.
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
        player.sendMessage(ChatColor.GREEN + "Se te ha dado un token secundario: " + tokenKey);
    }
}
