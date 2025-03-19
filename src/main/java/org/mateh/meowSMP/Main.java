package org.mateh.meowSMP;

import org.bukkit.plugin.java.JavaPlugin;
import org.mateh.meowSMP.commands.TokenCommand;
import org.mateh.meowSMP.completers.TokenCommandTabCompleter;
import org.mateh.meowSMP.data.SQLiteManager;
import org.mateh.meowSMP.listeners.*;
import org.mateh.meowSMP.managers.RecipeManager;

public final class Main extends JavaPlugin {
    private SQLiteManager sqliteManager;
    private TokenListener tokenListener;

    @Override
    public void onEnable() {
        sqliteManager = new SQLiteManager(getDataFolder().getAbsolutePath() + "/data.db");

        tokenListener = new TokenListener(this);
        getServer().getPluginManager().registerEvents(tokenListener, this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new BlackCatHitListener(this), this);
        getServer().getPluginManager().registerEvents(new TokenSpawnListener(this, tokenListener), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this, tokenListener), this);
        getServer().getPluginManager().registerEvents(new KillListener(this), this);
        getServer().getPluginManager().registerEvents(new TokenPickupListener(), this);
        getServer().getPluginManager().registerEvents(new DeathTokenListener(this, tokenListener), this);

        getCommand("token").setExecutor(new TokenCommand(this));
        getCommand("token").setTabCompleter(new TokenCommandTabCompleter());

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

    public TokenListener getTokenListener() {
        return tokenListener;
    }
}
