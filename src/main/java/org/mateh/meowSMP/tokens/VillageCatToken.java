package org.mateh.meowSMP.tokens;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.enums.TokenType;

import java.util.Arrays;

public class VillageCatToken extends AbstractToken {

    public VillageCatToken(Main main, SQLiteManager db) {
        super(main, db, "village_cat", TokenType.SECONDARY, true, 180, Arrays.asList(
                new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 16 * 20, 3)
        ));
    }

    @Override
    public void activateAbility(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 60 * 20, 9));
        player.sendMessage("Village Cat activated: Hero of the Village X for 1 minute.");
    }

    @Override
    public String getDisplayName() {
        return "Village Cat";
    }
}
