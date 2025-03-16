package org.mateh.meowSMP;

import org.bukkit.plugin.java.JavaPlugin;
import org.mateh.meowSMP.commands.TokenCommand;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.listeners.FallDamageListener;
import org.mateh.meowSMP.listeners.TokenListener;
import org.mateh.meowSMP.managers.RecipeManager;

public final class Main extends JavaPlugin {

    private SQLiteManager sqliteManager;

    @Override
    public void onEnable() {
        sqliteManager = new SQLiteManager(getDataFolder().getAbsolutePath() + "/data.db");

        getServer().getPluginManager().registerEvents(new TokenListener(this), this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(this), this);
        getCommand("token").setExecutor(new TokenCommand(this));

        RecipeManager.registerRecipes(this);
    }

    @Override
    public void onDisable() {
        if (sqliteManager != null) {
            sqliteManager.close();
        }
    }

    public SQLiteManager getSQLiteManager() {
        return sqliteManager;
    }
}
