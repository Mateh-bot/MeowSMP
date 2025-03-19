package org.mateh.meowSMP.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.TokenItems;

import java.util.Random;

public class PlayerJoinListener implements Listener {

    private final Main main;
    private final Random random = new Random();

    public PlayerJoinListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!hasToken(player)) {
            int sel = random.nextInt(4);
            switch (sel) {
                case 0:
                    player.getInventory().addItem(TokenItems.createCatfishTokenItem(main));
                    break;
                case 1:
                    player.getInventory().addItem(TokenItems.createVillageCatTokenItem(main));
                    break;
                case 2:
                    player.getInventory().addItem(TokenItems.createBlackCatTokenItem(main));
                    break;
                case 3:
                    player.getInventory().addItem(TokenItems.createJungleCatTokenItem(main));
                    break;
            }
        }
    }

    private boolean hasToken(Player player) {
        return player.getInventory().all(org.bukkit.Material.PAPER).values().stream().anyMatch(item -> {
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                String tokenKey = item.getItemMeta().getLore().get(0);
                return tokenKey.equals("catfish_token") || tokenKey.equals("village_cat") ||
                        tokenKey.equals("black_cat") || tokenKey.equals("jungle_cat") ||
                        tokenKey.equals("lion_token") || tokenKey.equals("cheetah_token") ||
                        tokenKey.equals("tiger_token") || tokenKey.equals("house_cat") ||
                        tokenKey.equals("sphinx_token");
            }
            return false;
        });
    }
}
