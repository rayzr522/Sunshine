package me.rayzr522.sunshine.event;

import me.rayzr522.sunshine.Sunshine;
import me.rayzr522.sunshine.struct.PlayerSettings;
import me.rayzr522.sunshine.utils.FormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {
    private final Sunshine plugin;

    private Map<UUID, Long> disconnectTime = new HashMap<>();

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

        long now = System.currentTimeMillis();
        if (!disconnectTime.containsKey(player.getUniqueId()) || now - disconnectTime.get(player.getUniqueId()) > TimeUnit.MINUTES.toMillis(plugin.getSettings().getDisconnectGraceTime())) {
            settings.setJoinTime(now);
            settings.setKickTime(-1);
            disconnectTime.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        disconnectTime.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
    }
}
