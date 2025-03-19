package org.mateh.meowSMP.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mateh.meowSMP.Main;

public class PlayerQuitListener implements Listener {
    private final Main main;
    private final TokenListener tokenListener;

    public PlayerQuitListener(Main main, TokenListener tokenListener) {
        this.main = main;
        this.tokenListener = tokenListener;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        tokenListener.resetCooldown(event.getPlayer());
    }
}
