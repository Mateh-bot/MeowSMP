package org.mateh.meowSMP.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.TokenItems;

public class RecipeManager {

    public static void registerRecipes(Main main) {
        // Sphinx Token Recipe: Only a dragon egg is needed.
        ItemStack sphinxToken = TokenItems.createSphinxTokenItem(main);
        NamespacedKey key = new NamespacedKey(main, "sphinx_token");
        ShapelessRecipe recipe = new ShapelessRecipe(key, sphinxToken);
        recipe.addIngredient(Material.DRAGON_EGG);
        Bukkit.addRecipe(recipe);
    }
}
