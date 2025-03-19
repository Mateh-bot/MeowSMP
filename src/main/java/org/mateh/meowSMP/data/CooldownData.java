package org.mateh.meowSMP.data;

import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

public class CooldownData {
    private final BukkitRunnable task;
    private final BossBar bossBar;

    public CooldownData(BukkitRunnable task, BossBar bossBar) {
        this.task = task;
        this.bossBar = bossBar;
    }

    public BukkitRunnable getTask() {
        return task;
    }

    public BossBar getBossBar() {
        return bossBar;
    }
}
