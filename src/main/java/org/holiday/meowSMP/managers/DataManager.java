package org.holiday.meowSMP.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.holiday.meowSMP.Main;
import org.holiday.meowSMP.data.PlayerData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {
    private final Main main;
    private final File file;
    private final YamlConfiguration config;
    private final Map<UUID, PlayerData> dataMap = new HashMap<>();

    public DataManager(Main main) {
        this.main = main;
        file = new File(main.getDataFolder(), "playerdata.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        loadAll();
    }

    public void loadAll() {
        for (String key : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                PlayerData data = config.getSerializable(key, PlayerData.class);
                if (data != null) {
                    dataMap.put(uuid, data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAll() {
        for (Map.Entry<UUID, PlayerData> entry : dataMap.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayer(UUID uuid) {
        config.set(uuid.toString(), dataMap.get(uuid));
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerData getPlayerData(UUID uuid) {
        return dataMap.get(uuid);
    }

    public void addPlayerData(UUID uuid, PlayerData data) {
        dataMap.put(uuid, data);
    }

    public Map<UUID, PlayerData> getAllPlayerData() {
        return dataMap;
    }
}
