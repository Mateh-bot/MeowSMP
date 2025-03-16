package org.mateh.meowSMP.tokens;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.enums.TokenType;

import java.util.Arrays;

public class LionToken extends AbstractToken {

    public LionToken(Main main, SQLiteManager db) {
        super(main, db, "lion_token", TokenType.PRIMARY, true, 30, Arrays.asList(
                new PotionEffect(PotionEffectType.SPEED, 16 * 20, 0),
                new PotionEffect(PotionEffectType.STRENGTH, 16 * 20, 1)
        ));
    }

    @Override
    public void activateAbility(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 10 * 20, 2));
        player.sendMessage("Lion Token activated: Strength III for 10 seconds.");
    }

    @Override
    public String getDisplayName() {
        return "Lion Token";
    }
}
