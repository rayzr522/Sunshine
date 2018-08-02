package me.rayzr522.sunshine.data;

import org.bukkit.configuration.ConfigurationSection;

public class Settings {
    private ConfigurationSection config;

    public void load(ConfigurationSection config) {
        this.config = config;
    }

    public long getCheckDelay() {
        return config.getLong("check-delay");
    }

    public int getDisconnectGraceTime() {
        return config.getInt("disconnect-grace-time");
    }
}
