package org.mateh.meowSMP.abstracts;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.enums.TokenType;

import java.util.List;

public abstract class AbstractToken {

    protected final Main main;
    protected final SQLiteManager db;
    protected final String key;
    protected final TokenType tokenType;
    protected final boolean triggerOnRightClick;
    protected final int cooldownSeconds;
    protected final List<PotionEffect> passiveEffects;

    public AbstractToken(Main main, SQLiteManager db, String key, TokenType tokenType,
                         boolean triggerOnRightClick, int cooldownSeconds, List<PotionEffect> passiveEffects) {
        this.main = main;
        this.db = db;
        this.key = key;
        this.tokenType = tokenType;
        this.triggerOnRightClick = triggerOnRightClick;
        this.cooldownSeconds = cooldownSeconds;
        this.passiveEffects = passiveEffects;
    }

    public String getKey() {
        return key;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public int getCooldown() {
        return cooldownSeconds;
    }

    public List<PotionEffect> getPassiveEffects() {
        return passiveEffects;
    }

    public boolean isValidAction(PlayerInteractEvent event) {
        return event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK;
    }

    public void showCooldownBossBar(Player player, int effectiveCooldown) {
        BossBar bossBar = Bukkit.createBossBar(getDisplayName() + " Cooldown: " + effectiveCooldown + " seconds", BarColor.RED, BarStyle.SOLID);
        bossBar.addPlayer(player);
        new BukkitRunnable() {
            int timeLeft = effectiveCooldown;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    bossBar.removeAll();
                    cancel();
                    return;
                }
                double progress = (double) timeLeft / effectiveCooldown;
                bossBar.setProgress(progress);
                bossBar.setTitle(getDisplayName() + " Cooldown: " + timeLeft + " seconds");
                timeLeft--;
            }
        }.runTaskTimer(main, 0L, 20L);
    }

    public void showCooldownBossBar(Player player) {
        showCooldownBossBar(player, cooldownSeconds);
    }

    public SQLiteManager.TokenData loadTokenData(Player player) {
        return db.loadTokenData(player.getUniqueId().toString(), key);
    }

    public void saveTokenData(Player player, int level, int kills) {
        db.saveTokenData(player.getUniqueId().toString(), key, level, kills);
    }

    public abstract void activateAbility(Player player);

    public abstract String getDisplayName();
}
