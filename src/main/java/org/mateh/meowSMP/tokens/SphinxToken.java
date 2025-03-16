package org.mateh.meowSMP.tokens;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.enums.TokenType;

import java.util.Arrays;

public class SphinxToken extends AbstractToken {

    public SphinxToken(Main main, SQLiteManager db) {
        super(main, db, "sphinx_token", TokenType.EVENT, true, 180, Arrays.asList(
                new PotionEffect(PotionEffectType.STRENGTH, 16 * 20, 1),
                new PotionEffect(PotionEffectType.SPEED, 16 * 20, 1),
                new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 16 * 20, 0),
                new PotionEffect(PotionEffectType.HEALTH_BOOST, 16 * 20, 4)
        ));
    }

    @Override
    public void activateAbility(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 30 * 20, 4));
        player.sendMessage("Sphinx Token activated: Resistance V for 30 seconds.");
    }

    @Override
    public String getDisplayName() {
        return "Sphinx Token";
    }
}
