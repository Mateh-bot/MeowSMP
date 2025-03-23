package org.holiday.meowSMP;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.holiday.meowSMP.commands.TokenCommand;
import org.holiday.meowSMP.data.PlayerData;
import org.holiday.meowSMP.enums.TokenType;
import org.holiday.meowSMP.listeners.*;
import org.holiday.meowSMP.managers.DataManager;
import org.holiday.meowSMP.managers.PlayerDataManager;
import org.holiday.meowSMP.utils.TokenUtils;

import java.util.UUID;

public final class Main extends JavaPlugin {
    private static Main main;
    private DataManager dataManager;

    @Override
    public void onEnable() {
        main = this;
        ConfigurationSerialization.registerClass(PlayerData.class, "PlayerData");
        saveDefaultConfig();

        dataManager = new DataManager(this);
        for (UUID uuid : dataManager.getAllPlayerData().keySet()) {
            PlayerData data = dataManager.getAllPlayerData().get(uuid);
            PlayerDataManager.addPlayerData(uuid, data);
        }
        TokenUtils.loadCooldowns(this);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(dataManager), this);

        // Registrar comandos
        TokenCommand tokenCommand = new TokenCommand();
        this.getCommand("token").setExecutor(tokenCommand);
        this.getCommand("token").setTabCompleter(tokenCommand);

        // Aplicar pasivas cada 3 segundos
        getServer().getScheduler().runTaskTimer(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                PlayerData data = PlayerDataManager.getPlayerData(player.getUniqueId());
                if (data != null) {
                    if (data.getPrimaryToken() != null) {
                        TokenUtils.applyPassiveEffects(player, data.getPrimaryToken());
                    }
                    if (data.getSecondaryToken() != null) {
                        TokenUtils.applyPassiveEffects(player, data.getSecondaryToken());
                    }
                }
            });
        }, 0L, 3 * 20L);

        // Action Bar con cooldown (se usa el token primario para calcular el cooldown)
        getServer().getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = PlayerDataManager.getPlayerData(player.getUniqueId());
                if (data == null) continue;

                String icons = "";
                if (data.getPrimaryToken() != null) {
                    icons += TokenUtils.getActionBarIcon(data.getPrimaryToken(), data.getPrimaryTokenLevel());
                }
                if (data.getSecondaryToken() != null) {
                    if (!icons.isEmpty()) {
                        icons += " ";
                    }
                    icons += TokenUtils.getActionBarIcon(data.getSecondaryToken(), 1);
                }
                if (icons.isEmpty()) continue;

                TokenType tokenForCooldown = data.getPrimaryToken() != null ? data.getPrimaryToken() : data.getSecondaryToken();
                long baseCooldown = TokenUtils.getCooldown(tokenForCooldown);
                long effectiveCooldown = baseCooldown;
                if (tokenForCooldown.isPrimary()) {
                    int level = data.getPrimaryTokenLevel();
                    double reductionFactor = 1.0 - ((level - 1) * 0.1);
                    effectiveCooldown = (long) (baseCooldown * reductionFactor);
                }

                Long lastUsed = data.getAbilityCooldown(tokenForCooldown);
                long current = System.currentTimeMillis();
                String message;
                if (lastUsed == null || current - lastUsed >= effectiveCooldown) {
                    message = "§aREADY";
                } else {
                    long remaining = effectiveCooldown - (current - lastUsed);
                    double fractionElapsed = (double) (current - lastUsed) / effectiveCooldown;
                    String color;
                    if (fractionElapsed < 0.5) {
                        color = "§c";
                    } else if (fractionElapsed < 0.75) {
                        color = "§6";
                    } else {
                        color = "§e";
                    }
                    long remainingSeconds = (remaining + 999) / 1000;
                    message = color + remainingSeconds + "s";
                }
                String actionBarMessage = icons + "  " + message;
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionBarMessage));
            }
        }, 0L, 20L);

        // Guardado periódico de data
        getServer().getScheduler().runTaskTimer(this, () -> {
            dataManager.saveAll();
        }, 20L * 60, 20L * 60);
    }

    @Override
    public void onDisable() {
        dataManager.saveAll();
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public static Main getInstance() {
        return main;
    }
}
