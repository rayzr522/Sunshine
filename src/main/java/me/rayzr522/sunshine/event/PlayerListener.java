package me.rayzr522.sunshine.event;

import me.rayzr522.sunshine.Sunshine;
import me.rayzr522.sunshine.struct.PlayerSettings;
import me.rayzr522.sunshine.utils.FormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {
    private final Sunshine plugin;

    public PlayerListener(Sunshine plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        if (player == null) {
            return;
        }

        PlayerSettings settings = plugin.getPlayerSettingsManager().getPlayerSettings(player.getUniqueId());
        int rejoinDelay = settings.getRejoinDelay();
        if (rejoinDelay < 1) {
            return;
        }

        long kickTime = settings.getKickTime();
        if (kickTime < 0) {
            return;
        }

        long now = System.currentTimeMillis();
        long diff = now - kickTime;

        if (diff < TimeUnit.MINUTES.toMillis(rejoinDelay)) {
            // Tell them how much time they have left.
            String message = plugin.tr("system.kicked", FormatUtils.time(rejoinDelay - (int) TimeUnit.MILLISECONDS.toMinutes(diff)));
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, message);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerSettings settings = plugin.getPlayerSettingsManager().getPlayerSettings(player.getUniqueId());

        // TODO: Don't reset join time if the player has only been off for 10-15 minutes (configurable?) to account for game crashing, etc.
        settings.setJoinTime(System.currentTimeMillis());
        settings.setKickTime(-1);
    }
}
