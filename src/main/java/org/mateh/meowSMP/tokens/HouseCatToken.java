package org.mateh.meowSMP.tokens;

import org.bukkit.Particle;
import org.bukkit.Sound;
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
        Vector current = player.getVelocity();
        player.setVelocity(new Vector(current.getX(), 2.6, current.getZ()));
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.1);
        player.sendMessage("House Cat activated: Jumping upward.");
    }

    @Override
    public String getDisplayName() {
        return "House Cat";
    }
}
