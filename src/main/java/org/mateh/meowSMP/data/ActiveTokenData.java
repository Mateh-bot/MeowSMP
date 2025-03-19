package org.mateh.meowSMP.data;

public class ActiveTokenData {
    private final String tokenKey;
    private final int level;
    private final int kills;
    private final long cooldownEnd;

    public ActiveTokenData(String tokenKey, int level, int kills, long cooldownEnd) {
        this.tokenKey = tokenKey;
        this.level = level;
        this.kills = kills;
        this.cooldownEnd = cooldownEnd;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public int getLevel() {
        return level;
    }

    public int getKills() {
        return kills;
    }

    public long getCooldownEnd() {
        return cooldownEnd;
    }
}
