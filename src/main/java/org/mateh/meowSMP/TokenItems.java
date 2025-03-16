package org.mateh.meowSMP;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TokenItems {

    public static ItemStack createTokenItem(Main main, String tokenKey, String displayName, int customModelData) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + displayName);
        meta.setCustomModelData(customModelData);
        meta.setLore(Arrays.asList(tokenKey));
        item.setItemMeta(meta);
        return item;
    }

    // Primary tokens
    public static ItemStack createLionTokenItem(Main main) {
        return createTokenItem(main, "lion_token", "Lion Token", 1001);
    }
    public static ItemStack createCheetahTokenItem(Main main) {
        return createTokenItem(main, "cheetah_token", "Cheetah Token", 1002);
    }
    public static ItemStack createTigerTokenItem(Main main) {
        return createTokenItem(main, "tiger_token", "Tiger Token", 1003);
    }
    public static ItemStack createHouseCatTokenItem(Main main) {
        return createTokenItem(main, "house_cat", "House Cat", 1004);
    }

    // Secondary tokens
    public static ItemStack createCatfishTokenItem(Main main) {
        return createTokenItem(main, "catfish_token", "Catfish Token", 2001);
    }
    public static ItemStack createVillageCatTokenItem(Main main) {
        return createTokenItem(main, "village_cat", "Village Cat", 2002);
    }
    public static ItemStack createBlackCatTokenItem(Main main) {
        return createTokenItem(main, "black_cat", "Black Cat", 2003);
    }
    public static ItemStack createJungleCatTokenItem(Main main) {
        return createTokenItem(main, "jungle_cat", "Jungle Cat", 2004);
    }

    // Event token
    public static ItemStack createSphinxTokenItem(Main main) {
        return createTokenItem(main, "sphinx_token", "Sphinx Token", 3001);
    }
}
