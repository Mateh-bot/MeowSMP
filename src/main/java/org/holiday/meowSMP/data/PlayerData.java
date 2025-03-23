package org.holiday.meowSMP.data;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.holiday.meowSMP.enums.TokenType;

import java.util.HashMap;
import java.util.Map;

public class PlayerData implements ConfigurationSerializable {
    private TokenType primaryToken;
    private TokenType secondaryToken;
    private int killCount;
    private int primaryTokenLevel = 1;
    private final Map<TokenType, Long> abilityCooldowns = new HashMap<>();
    private boolean hasReceivedInitialToken = false;

    public PlayerData() {
    }

    public TokenType getPrimaryToken() {
        return primaryToken;
    }

    public void setPrimaryToken(TokenType primaryToken) {
        this.primaryToken = primaryToken;
    }

    public TokenType getSecondaryToken() {
        return secondaryToken;
    }

    public void setSecondaryToken(TokenType secondaryToken) {
        this.secondaryToken = secondaryToken;
    }

    public int getKillCount() {
        return killCount;
    }

    public void incrementKillCount() {
        killCount++;
    }

    public int getPrimaryTokenLevel() {
        return primaryTokenLevel;
    }

    public void incrementPrimaryTokenLevel() {
        primaryTokenLevel++;
    }

    public Long getAbilityCooldown(TokenType token) {
        return abilityCooldowns.get(token);
    }

    public void setAbilityCooldown(TokenType token, long time) {
        abilityCooldowns.put(token, time);
    }

    public boolean hasReceivedInitialToken() {
        return hasReceivedInitialToken;
    }

    public void setHasReceivedInitialToken(boolean received) {
        this.hasReceivedInitialToken = received;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("primaryToken", primaryToken == null ? null : primaryToken.name());
        map.put("secondaryToken", secondaryToken == null ? null : secondaryToken.name());
        map.put("killCount", killCount);
        map.put("primaryTokenLevel", primaryTokenLevel);
        map.put("hasReceivedInitialToken", hasReceivedInitialToken);
        Map<String, Long> cooldowns = new HashMap<>();
        for (Map.Entry<TokenType, Long> entry : abilityCooldowns.entrySet()) {
            cooldowns.put(entry.getKey().name(), entry.getValue());
        }
        map.put("abilityCooldowns", cooldowns);
        return map;
    }

    public static PlayerData deserialize(Map<String, Object> map) {
        PlayerData data = new PlayerData();
        if (map.get("primaryToken") != null) {
            data.primaryToken = TokenType.valueOf((String) map.get("primaryToken"));
        }
        if (map.get("secondaryToken") != null) {
            data.secondaryToken = TokenType.valueOf((String) map.get("secondaryToken"));
        }
        data.killCount = (Integer) map.get("killCount");
        data.primaryTokenLevel = (Integer) map.get("primaryTokenLevel");
        data.hasReceivedInitialToken = map.get("hasReceivedInitialToken") != null
                && (Boolean) map.get("hasReceivedInitialToken");
        Map<String, Object> cooldowns = (Map<String, Object>) map.get("abilityCooldowns");
        if (cooldowns != null) {
            for (Map.Entry<String, Object> entry : cooldowns.entrySet()) {
                data.abilityCooldowns.put(TokenType.valueOf(entry.getKey()), ((Number) entry.getValue()).longValue());
            }
        }
        return data;
    }
}
