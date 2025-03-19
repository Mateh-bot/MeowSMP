package org.mateh.meowSMP;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.Arrays;
import java.util.List;

public class TokenItems {

    public static ItemStack createTokenItem(Main main, String tokenKey, String displayName) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + displayName);
        CustomModelDataComponent dataComponent = meta.getCustomModelDataComponent();
        dataComponent.setStrings(List.of(tokenKey));
        meta.setCustomModelDataComponent(dataComponent);
        meta.setLore(Arrays.asList(tokenKey));
        item.setItemMeta(meta);
        return item;
    }

    // Primary tokens
    public static ItemStack createLionTokenItem(Main main) {
        return createTokenItem(main, "lion_token", "Lion Token");
    }

    public static ItemStack createCheetahTokenItem(Main main) {
        return createTokenItem(main, "cheetah_token", "Cheetah Token");
    }

    public static ItemStack createTigerTokenItem(Main main) {
        return createTokenItem(main, "tiger_token", "Tiger Token");
    }

    public static ItemStack createHouseCatTokenItem(Main main) {
        return createTokenItem(main, "house_cat", "House Cat");
    }

    // Secondary tokens
    public static ItemStack createCatfishTokenItem(Main main) {
        return createTokenItem(main, "catfish_token", "Catfish Token");
    }

    public static ItemStack createVillageCatTokenItem(Main main) {
        return createTokenItem(main, "village_cat", "Village Cat");
    }

    public static ItemStack createBlackCatTokenItem(Main main) {
        return createTokenItem(main, "black_cat", "Black Cat");
    }

    public static ItemStack createJungleCatTokenItem(Main main) {
        return createTokenItem(main, "jungle_cat", "Jungle Cat");
    }

    // Event token
    public static ItemStack createSphinxTokenItem(Main main) {
        return createTokenItem(main, "sphinx_token", "Sphinx Token");
    }
}