package me.rayzr522.sunshine.struct;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class PlayerSettings {
    private long playtimeLimit = 120L * 60L * 20L;
    private long warningBufferTime = 10L * 60L * 20L;
    private String warningMessage;

    public PlayerSettings() {
        // For use in creating default player settings for a player
    }

    private PlayerSettings(long playtimeLimit, long warningBufferTime, String warningMessage) {
        this.playtimeLimit = playtimeLimit;
        this.warningBufferTime = warningBufferTime;
        this.warningMessage = warningMessage;
    }

    public static PlayerSettings deserialize(ConfigurationSection config) {
        return new PlayerSettings(
                config.getLong("playtime-limit", 120L * 60L * 20L),
                config.getLong("warning-buffer-time", 10L * 60L * 20L),
                config.getString("warning-message")
        );
    }

    public long getPlaytimeLimit() {
        return playtimeLimit;
    }

    public long getWarningBufferTime() {
        return warningBufferTime;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> output = new HashMap<>();
        output.put("playtime-limit", playtimeLimit);
        output.put("warning-buffer-time", warningBufferTime);
        output.put("warning-message", warningMessage);
        return output;
    }
}
