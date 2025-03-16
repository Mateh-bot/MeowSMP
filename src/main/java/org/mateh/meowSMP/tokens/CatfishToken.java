package org.mateh.meowSMP.tokens;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.enums.TokenType;

import java.util.Arrays;

public class CatfishToken extends AbstractToken {

    public CatfishToken(Main main, SQLiteManager db) {
        super(main, db, "catfish_token", TokenType.SECONDARY, true, 30, Arrays.asList(
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 16 * 20, 0),
                new PotionEffect(PotionEffectType.CONDUIT_POWER, 16 * 20, 0)
        ));
    }

    @Override
    public void activateAbility(Player player) {
        if (player.getWorld().hasStorm() || player.isInWater()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 15 * 20, 4));
            player.sendMessage("Catfish Token activated: Strength V for 15 seconds.");
        } else {
            player.sendMessage("Catfish Token: Ability can only be activated when raining or in water.");
        }
    }

    @Override
    public String getDisplayName() {
        return "Catfish Token";
    }
}
