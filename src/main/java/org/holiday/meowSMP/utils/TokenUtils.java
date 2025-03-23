package org.holiday.meowSMP.utils;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.holiday.meowSMP.Main;
import org.holiday.meowSMP.enums.TokenType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenUtils {

    private static final Map<TokenType, Long> configCooldowns = new HashMap<>();

    public static void loadCooldowns(Main main) {
        for (TokenType token : TokenType.values()) {
            long defaultCooldownSec = token.getAbilityCooldown() / 1000;
            long cooldownSeconds = main.getConfig().getLong("cooldowns." + token.name(), defaultCooldownSec);
            configCooldowns.put(token, cooldownSeconds * 1000);
        }
    }

    public static long getCooldown(TokenType token) {
        return configCooldowns.getOrDefault(token, token.getAbilityCooldown());
    }

    public static boolean isTokenItem(ItemStack item) {
        if (item == null || item.getType() != Material.PAPER) return false;
        if (!item.hasItemMeta() || item.getItemMeta().getDisplayName() == null) return false;
        String displayName = item.getItemMeta().getDisplayName();
        for (TokenType token : TokenType.values()) {
            if (displayName.contains(token.getDisplayName().trim())) {
                return true;
            }
        }
        return false;
    }

    public static TokenType getTokenTypeFromItem(ItemStack item) {
        if (item == null || !item.hasItemMeta() || item.getItemMeta().getDisplayName() == null)
            return null;
        String displayName = item.getItemMeta().getDisplayName();
        for (TokenType token : TokenType.values()) {
            if (displayName.contains(token.getDisplayName().trim())) {
                return token;
            }
        }
        return null;
    }

    public static ItemStack createTokenItem(TokenType token) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(token.getDisplayName());
        if (token.isPrimary()) {
            meta.setLore(List.of("Level: 1", "Cooldown reduction: 0%"));
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createTokenItem(TokenType token, int level) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(token.getDisplayName());
        if (token.isPrimary()) {
            int customModelData = getCustomModelDataForToken(token, level);
            meta.setCustomModelData(customModelData);
            meta.setLore(List.of("Level: " + level, "Cooldown reduction: " + ((level - 1) * 10) + "%"));
        }
        item.setItemMeta(meta);
        return item;
    }

    public static void applyTokenEffects(Player player, TokenType token) {
        applyPassiveEffects(player, token);
    }

    private static int getCustomModelDataForToken(TokenType token, int level) {
        switch(token) {
            case LION:
                return 1000 + level;
            case CHEETAH:
                return 2000 + level;
            case TIGER:
                return 3000 + level;
            case HOUSE_CAT:
                return 4000 + level;
            case SPHINX:
                return 5000 + level;
            default:
                return 0;
        }
    }

    public static String getActionBarIcon(TokenType token, int level) {
        if(token == TokenType.LION) {
            switch(level) {
                case 1: return "\ue000";
                case 2: return "\ue001";
                case 3: return "\ue002";
                case 4: return "\ue003";
                default: return "\ue000";
            }
        }
        if(token == TokenType.CHEETAH) {
            switch(level) {
                case 1: return "\ue010";
                case 2: return "\ue011";
                case 3: return "\ue012";
                case 4: return "\ue013";
                default: return "\ue010";
            }
        }
        if(token == TokenType.TIGER) {
            switch(level) {
                case 1: return "\ue020";
                case 2: return "\ue021";
                case 3: return "\ue022";
                case 4: return "\ue023";
                default: return "\ue020";
            }
        }
        if(token == TokenType.HOUSE_CAT) {
            switch(level) {
                case 1: return "\ue030";
                case 2: return "\ue031";
                case 3: return "\ue032";
                case 4: return "\ue033";
                default: return "\ue030";
            }
        }
        if(token == TokenType.CATFISH) return "\ue040";
        if(token == TokenType.VILLAGE_CAT) return "\ue041";
        if(token == TokenType.BLACK_CAT) return "\ue042";
        if(token == TokenType.JUNGLE_CAT) return "\ue043";
        if(token == TokenType.SPHINX) return "\ue050";
        return "";
    }

    public static void applyPassiveEffects(Player player, TokenType tokenType) {
        switch (tokenType) {
            case LION:
                player.setMaxHealth(20);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 5 * 20, 1));
                break;
            case CHEETAH:
                player.setMaxHealth(20);
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 5 * 20, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 1));
                break;
            case TIGER:
                player.setMaxHealth(30);
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 5 * 20, 1));
                break;
            case HOUSE_CAT:
                player.setMaxHealth(20);
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 5 * 20, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 5 * 20, 1));
                break;
            case CATFISH:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 5 * 20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 5 * 20, 0));
                break;
            case VILLAGE_CAT:
                player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 5 * 20, 3));
                break;
            case BLACK_CAT:
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 5 * 20, 0));
                break;
            case JUNGLE_CAT:
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5 * 20, 0));
                break;
            case SPHINX:
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 5 * 20, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5 * 20, 0));
                player.setMaxHealth(40);
                break;
        }
    }

    public static void triggerAbility(Player player, TokenType tokenType) {
        switch (tokenType) {
            case LION:
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 10 * 20, 2));
                player.playSound(player.getLocation(), Sound.ENTITY_CAT_HURT, 1.0f, 0.5f);
                player.sendMessage(ChatColor.GREEN + "Lion Token activated: Strength III for 10 seconds.");
                break;
            case CHEETAH:
                World world = player.getWorld();
                Location origin = player.getLocation();
                Vector direction = origin.getDirection().setY(0).normalize();
                Location target = origin.clone().add(direction.multiply(20));

                int safeY = world.getHighestBlockYAt(target.getBlockX(), target.getBlockZ()) + 1;
                Location safeTarget = new Location(world, target.getX(), safeY, target.getZ());

                while (!safeTarget.getBlock().isPassable() && safeTarget.getY() < world.getMaxHeight()) {
                    safeTarget.add(0, 1, 0);
                }

                safeTarget.setYaw(origin.getYaw());
                safeTarget.setPitch(origin.getPitch());

                world.playSound(safeTarget, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);
                world.spawnParticle(Particle.CLOUD, safeTarget, 30, 0.5, 0.5, 0.5, 0.1);

                player.teleport(safeTarget);
                player.sendMessage(ChatColor.GREEN + "Cheetah Token activated: Dashing forward.");
                break;
            case TIGER:
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30 * 20, 0));
                player.playSound(player.getLocation(), Sound.ENTITY_CAT_STRAY_AMBIENT, 1.0f, 1.0f);
                player.sendMessage(ChatColor.GREEN + "Tiger Token activated: Invisibility for 30 seconds.");
                break;
            case HOUSE_CAT:
                Vector current = player.getVelocity();
                player.setVelocity(new Vector(current.getX(), 2.6, current.getZ()));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);
                player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.1);
                player.sendMessage(ChatColor.GREEN + "House Cat activated: Jumping upward.");
                break;
            case CATFISH:
                if (player.getWorld().hasStorm() || player.isInWater()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 15 * 20, 4));
                    player.playSound(player.getLocation(), Sound.ENTITY_TROPICAL_FISH_DEATH, 1.0f, 1.0f);
                    player.sendMessage(ChatColor.GREEN + "Catfish Token activated: Strength V for 15 seconds.");
                } else {
                    player.sendMessage(ChatColor.RED + "Catfish Token: Ability can only be activated when raining or in water.");
                }
                break;
            case VILLAGE_CAT:
                player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 60 * 20, 9));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 1.0f);
                player.sendMessage(ChatColor.GREEN + "Village Cat activated: Hero of the Village X for 1 minute.");
                break;
            case BLACK_CAT:
                player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_CAT_HISS, 1.0f, 1.0f);
                player.getWorld().spawnParticle(Particle.WITCH, player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);
                player.sendMessage(ChatColor.GREEN + "Black Cat activated: Your next hit has a 20% chance to apply Poison II and Weakness II.");
                break;
            case JUNGLE_CAT:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 3));
                player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_JUMP, 1.0f, 1.0f);
                player.sendMessage(ChatColor.GREEN + "Jungle Cat activated: Speed IV for 10 seconds.");
                break;
            case SPHINX:
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 30 * 20, 4));
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.5f, 1.0f);
                player.sendMessage(ChatColor.GREEN + "Sphinx Token activated: Resistance V for 30 seconds.");
                break;
        }
    }
}
