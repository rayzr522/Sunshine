package me.rayzr522.sunshine.struct;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerSettings {
    private transient long joinTime = -1L;
    private transient long kickTime = -1L;

    private boolean enabled = false;
    private int playtimeLimit = 120;
    private int warningBufferTime = 10;
    private String warningMessage;

    public PlayerSettings() {
        // For use in creating default player settings for a player
    }

    private PlayerSettings(boolean enabled, int playtimeLimit, int warningBufferTime, String warningMessage) {
        this.enabled = enabled;
        this.playtimeLimit = playtimeLimit;
        this.warningBufferTime = warningBufferTime;
        this.warningMessage = warningMessage;
    }

    /**
     * Loads a player settings object from a config.
     *
     * @param config The config to load from.
     * @return The loaded player settings object.
     */
    public static PlayerSettings deserialize(ConfigurationSection config) {
        return new PlayerSettings(
                config.getBoolean("enabled"),
                config.getInt("playtime-limit", 120),
                config.getInt("warning-buffer-time", 10),
                config.getString("warning-message")
        );
    }

    /**
     * @return When the player joined, in milliseconds (system time).
     */
    public long getJoinTime() {
        return joinTime;
    }

    /**
     * Updates the player's join time.
     *
     * @param joinTime The new join time to set, in milliseconds.
     */
    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    /**
     * @return When the player was kicked by Sunshine, in milliseconds (system time).
     */
    public long getKickTime() {
        return kickTime;
    }

    /**
     * Updates the time at which Sunshine kicked the player.
     *
     * @param kickTime The new kick time to set, in milliseconds.
     */
    public void setKickTime(long kickTime) {
        this.kickTime = kickTime;
    }

    /**
     * @return Whether or not Sunshine is enabled for this player.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @return This player's playtime limit, in minutes.
     */
    public int getPlaytimeLimit() {
        return playtimeLimit;
    }

    /**
     * This is how many minutes before their time is up to show a warning.
     *
     * @return This player's warning buffer, in minutes.
     */
    public int getWarningBufferTime() {
        return warningBufferTime;
    }

    /**
     * @return The custom warning message to show the user.
     */
    public Optional<String> getWarningMessage() {
        return Optional.ofNullable(warningMessage)
                // TODO: Move color code translation to elsewhere?
                .map(message -> ChatColor.translateAlternateColorCodes('&', message));
    }

    public Map<String, Object> serialize() {
        Map<String, Object> output = new HashMap<>();
        output.put("enabled", enabled);
        output.put("playtime-limit", playtimeLimit);
        output.put("warning-buffer-time", warningBufferTime);
        output.put("warning-message", warningMessage);
        return output;
    }
}
