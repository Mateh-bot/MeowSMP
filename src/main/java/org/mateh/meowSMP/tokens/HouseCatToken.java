package org.mateh.meowSMP.tokens;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.enums.TokenType;

import java.util.Arrays;

public class HouseCatToken extends AbstractToken {

    public HouseCatToken(Main main, SQLiteManager db) {
        super(main, db, "house_cat", TokenType.PRIMARY, true, 60, Arrays.asList(
                new PotionEffect(PotionEffectType.JUMP_BOOST, 16 * 20, 1),
                new PotionEffect(PotionEffectType.RESISTANCE, 16 * 20, 1)
        ));
    }

    @Override
    public void activateAbility(Player player) {
        // Propels the player upward by half the previous impulse.
        player.setVelocity(new Vector(0, 1, 0).multiply(15));
        player.sendMessage("House Cat activated: Propelling upward.");
    }

    @Override
    public String getDisplayName() {
        return "House Cat";
    }
}
