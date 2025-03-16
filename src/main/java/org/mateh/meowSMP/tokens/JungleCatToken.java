package org.mateh.meowSMP.tokens;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.enums.TokenType;

import java.util.Arrays;

public class JungleCatToken extends AbstractToken {

    public JungleCatToken(Main main, SQLiteManager db) {
        super(main, db, "jungle_cat", TokenType.SECONDARY, true, 40, Arrays.asList(
                new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 16 * 20, 0)
        ));
    }

    @Override
    public void activateAbility(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 3));
        player.sendMessage("Jungle Cat activated: Speed IV for 10 seconds.");
    }

    @Override
    public String getDisplayName() {
        return "Jungle Cat";
    }
}
