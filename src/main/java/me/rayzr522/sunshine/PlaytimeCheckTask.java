package me.rayzr522.sunshine;

import me.rayzr522.sunshine.struct.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class PlaytimeCheckTask extends BukkitRunnable {
    private final Sunshine plugin;

    public PlaytimeCheckTask(Sunshine plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();

        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerSettings settings = plugin.getPlayerSettingsManager().getPlayerSettings(player.getUniqueId());
            if (!settings.isEnabled()) {
                return;
            }

            long joinTime = settings.getJoinTime();
            long diff = currentTime - joinTime;

            if (diff > TimeUnit.MINUTES.toMillis(settings.getPlaytimeLimit())) {
                // TODO: Kick them.
            } else if (settings.getWarningBufferTime() > 0 && diff > TimeUnit.MINUTES.toMillis(settings.getPlaytimeLimit() - settings.getWarningBufferTime())) {
                // TODO: Warn them.
            }
        });
    }
}
