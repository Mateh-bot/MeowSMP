package org.mateh.meowSMP.tokens;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.enums.TokenType;

import java.util.Arrays;

public class CheetahToken extends AbstractToken {

    public CheetahToken(Main main, SQLiteManager db) {
        super(main, db, "cheetah_token", TokenType.PRIMARY, true, 30, Arrays.asList(
                new PotionEffect(PotionEffectType.HASTE, 16 * 20, 1),
                new PotionEffect(PotionEffectType.SPEED, 16 * 20, 1)
        ));
    }

    @Override
    public void activateAbility(Player player) {
        Vector direction = player.getLocation().getDirection().setY(0).normalize().multiply(20);
        Location targetLocation = player.getLocation().clone().add(direction);
        targetLocation.setY(player.getLocation().getY());
        player.teleport(targetLocation);
        player.sendMessage("Cheetah Token activated: Dashing 20 blocks forward.");
    }

    @Override
    public String getDisplayName() {
        return "Cheetah Token";
    }
}
