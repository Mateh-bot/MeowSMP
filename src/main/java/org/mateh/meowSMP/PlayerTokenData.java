package org.mateh.meowSMP;

public class PlayerTokenData {
    private int killCount;
    private int level;
    private double baseCooldown;

    public PlayerTokenData(double baseCooldown) {
        this.killCount = 0;
        this.level = 1;
        this.baseCooldown = baseCooldown;
    }

    public int getKillCount() {
        return killCount;
    }

    public void incrementKillCount() {
        killCount++;
    }

    public int getLevel() {
        return level;
    }

    public void levelUp() {
        level++;
    }

    public double getCurrentCooldown() {
        return baseCooldown * Math.pow(0.9, level - 1);
    }
}
