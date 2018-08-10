package me.matamor.custominventories.utils;

import org.bukkit.scheduler.BukkitTask;

public abstract class BasicTaskHandler {

    public abstract BukkitTask createTask();

    private BukkitTask bukkitTask;

    public void start() {
        if (!isRunning()) {
            this.bukkitTask = createTask();
        }
    }

    public void stop() {
        if (isRunning()) {
            this.bukkitTask.cancel();
            this.bukkitTask = null;
        }
    }

    public boolean isRunning() {
        return this.bukkitTask != null && this.bukkitTask.getTaskId() != -1;
    }
}
