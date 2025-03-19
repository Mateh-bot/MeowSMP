package org.mateh.meowSMP.tokens;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.data.TokenData;
import org.mateh.meowSMP.enums.TokenType;

import java.util.Arrays;

public class CatfishToken extends AbstractToken {

    public CatfishToken(Main main, SQLiteManager db) {
        super(main, db, "catfish_token", TokenType.SECONDARY, true, 50, Arrays.asList(
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 16 * 20, 0),
                new PotionEffect(PotionEffectType.CONDUIT_POWER, 16 * 20, 0)
        ));
    }

    @Override
    public void activateAbility(Player player) {
        TokenData data = loadTokenData(player);
        int effectiveCooldown = cooldownSeconds;
        if (data != null) {
            int level = data.getLevel();
            effectiveCooldown = (int) Math.ceil(cooldownSeconds * Math.pow(0.9, level - 1));
        }

        if (player.getWorld().hasStorm() || player.isInWater()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 15 * 20, 4));
            player.playSound(player.getLocation(), Sound.ENTITY_TROPICAL_FISH_DEATH, 1.0f, 0.5f);
            showCooldownBossBar(player, effectiveCooldown);
            player.sendMessage("Catfish Token activated: Strength V for 15 seconds. Cooldown: " + effectiveCooldown + " seconds.");
        } else {
            player.sendMessage("Catfish Token: Ability can only be activated when raining or in water.");
        }
    }

    @Override
    public String getDisplayName() {
        return "Catfish Token";
    }
}
