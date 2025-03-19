package org.mateh.meowSMP.tokens;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.abstracts.AbstractToken;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.enums.TokenType;

import java.util.Arrays;

public class CheetahToken extends AbstractToken {

    public CheetahToken(Main main, SQLiteManager db) {
        super(main, db, "cheetah_token", TokenType.PRIMARY, true, 10, Arrays.asList(
                new PotionEffect(PotionEffectType.HASTE, 16 * 20, 1),
                new PotionEffect(PotionEffectType.SPEED, 16 * 20, 1)
        ));
    }

    @Override
    public void activateAbility(Player player) {
        World world = player.getWorld();
        Location origin = player.getLocation();
        Vector direction = origin.getDirection().setY(0).normalize();
        Location target = origin.clone().add(direction.multiply(20));

        int safeY = world.getHighestBlockYAt(target.getBlockX(), target.getBlockZ()) + 1;
        Location safeTarget = new Location(world, target.getX(), safeY, target.getZ());

        while (!safeTarget.getBlock().isPassable() && safeTarget.getY() < world.getMaxHeight()) {
            safeTarget.add(0, 1, 0);
        }

        world.playSound(safeTarget, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);
        world.spawnParticle(Particle.CLOUD, safeTarget, 30, 0.5, 0.5, 0.5, 0.1);

        player.teleport(safeTarget);
        player.sendMessage("Cheetah Token activated: Dashing forward safely.");
    }

    @Override
    public String getDisplayName() {
        return "Cheetah Token";
    }
}
