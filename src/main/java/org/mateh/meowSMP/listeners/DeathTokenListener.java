package org.mateh.meowSMP.listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.mateh.meowSMP.Main;

public class DeathTokenListener implements Listener {

    private final Main main;
    private final TokenListener tokenListener;

    public DeathTokenListener(Main main, TokenListener tokenListener) {
        this.main = main;
        this.tokenListener = tokenListener;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        tokenListener.resetCooldown(player);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20.0);
        if (tokenListener.getActiveToken(player) != null) {
            main.getSQLiteManager().setActiveToken(player.getUniqueId().toString(),
                    tokenListener.getActiveToken(player).getKey(), 1, 0, System.currentTimeMillis());
        }
    }
}
