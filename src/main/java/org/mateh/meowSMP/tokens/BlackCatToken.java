package org.mateh.meowSMP.tokens;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.enums.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BlackCatToken extends AbstractToken {

    public static Map<String, Boolean> activePlayers = new HashMap<>();

    public BlackCatToken(Main main, SQLiteManager db) {
        super(main, db, "black_cat", TokenType.SECONDARY, true, 30, Arrays.asList(
                new PotionEffect(PotionEffectType.RESISTANCE, 16 * 20, 3)
        ));
    }

    @Override
    public void activateAbility(Player player) {
        activePlayers.put(player.getUniqueId().toString(), true);
        player.sendMessage("Black Cat activated: Your next hit has a 20% chance to apply Poison II and Weakness II.");
        player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_CAT_HISS, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.WITCH, player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);
    }

    @Override
    public String getDisplayName() {
        return "Black Cat";
    }
}
