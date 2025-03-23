package org.holiday.meowSMP.managers;

import org.holiday.meowSMP.data.PlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private static final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public static PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.get(uuid);
    }

    public static void addPlayerData(UUID uuid, PlayerData data) {
        playerDataMap.put(uuid, data);
    }
}
