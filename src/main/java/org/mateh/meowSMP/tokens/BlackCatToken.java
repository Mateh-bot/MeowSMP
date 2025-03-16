package org.mateh.meowSMP.tokens;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.enums.TokenType;

import java.util.Arrays;

public class BlackCatToken extends AbstractToken {

    public BlackCatToken(Main main, SQLiteManager db) {
        super(main, db, "black_cat", TokenType.SECONDARY, true, 0, Arrays.asList(
                new PotionEffect(PotionEffectType.RESISTANCE, 16 * 20, 3)
        ));
    }

    @Override
    public void activateAbility(Player player) {
        player.sendMessage("Black Cat: Passive ability applies on hit (20% chance to poison and weaken).");
    }

    @Override
    public String getDisplayName() {
        return "Black Cat";
    }
}
