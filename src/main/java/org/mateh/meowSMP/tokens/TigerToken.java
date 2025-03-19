package org.mateh.meowSMP.tokens;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.enums.TokenType;

import java.util.Arrays;

public class TigerToken extends AbstractToken {

    public TigerToken(Main main, SQLiteManager db) {
        super(main, db, "tiger_token", TokenType.PRIMARY, true, 60, Arrays.asList(
                new PotionEffect(PotionEffectType.STRENGTH, 16 * 20, 1)
        ));
    }

    @Override
    public void activateAbility(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30 * 20, 0));
        player.playSound(player.getLocation(), Sound.ENTITY_CAT_STRAY_AMBIENT, 1.0f, 0.5f);
        player.sendMessage("Tiger Token activated: Invisibility for 30 seconds.");
    }

    @Override
    public String getDisplayName() {
        return "Tiger Token";
    }
}
