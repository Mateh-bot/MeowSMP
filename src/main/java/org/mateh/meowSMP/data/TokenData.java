package org.mateh.meowSMP.data;

public class TokenData {
    private final int level;
    private final int kills;

    public TokenData(int level, int kills) {
        this.level = level;
        this.kills = kills;
    }

    public int getLevel() {
        return level;
    }

    public int getKills() {
        return kills;
    }
}
