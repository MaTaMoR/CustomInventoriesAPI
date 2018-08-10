package me.matamor.custominventories.inventories;

import lombok.Getter;
import me.matamor.custominventories.utils.BasicTaskHandler;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import me.matamor.custominventories.CustomInventories;
import me.matamor.custominventories.utils.Size;

import java.util.concurrent.TimeUnit;

public class SimpleUpdatingCustomInventory extends SimpleCustomInventory implements CustomUpdatingInventory {

    @Getter
    private final long ticks;

    @Getter
    private final BasicTaskHandler taskHandler;

    public SimpleUpdatingCustomInventory(TimeUnit timeUnit, long delay) {
        this(Size.ONE_LINE, "", timeUnit, delay);
    }

    public SimpleUpdatingCustomInventory(Size size, String title, TimeUnit timeUnit, long delay) {
        super(size, title);

        this.ticks = timeUnit.convert(delay, TimeUnit.MILLISECONDS) / 50;
        this.taskHandler = new BasicTaskHandler() {
            @Override
            public BukkitTask createTask() {
                return new BukkitRunnable() {
                    @Override
                    public void run() {
                        update();
                    }
                }.runTaskTimer(CustomInventories.getPlugin(), ticks, ticks);
            }
        };
    }

    @Override
    public void onOpen(Player player) {
        this.taskHandler.start();
    }

    @Override
    public void onClose(Player player) {
        if (this.taskHandler.isRunning() && getViewers().isEmpty()) {
            this.taskHandler.stop();
        }
    }
}
