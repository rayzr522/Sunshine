package me.rayzr522.sunshine.data;

import me.rayzr522.sunshine.struct.PlayerSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerSettingsManager {
    private Map<UUID, PlayerSettings> playerSettingsMap;

    public void load(ConfigurationSection config) {
        playerSettingsMap = config.getKeys(false).stream().collect(Collectors.toMap(
                UUID::fromString,
                key -> PlayerSettings.deserialize(config.getConfigurationSection(key))
        ));
    }

    public YamlConfiguration save() {
        YamlConfiguration config = new YamlConfiguration();
        playerSettingsMap.forEach((id, playerSettings) -> config.createSection(id.toString(), playerSettings.serialize()));
        return config;
    }

    public PlayerSettings getPlayerSettings(UUID id) {
        return playerSettingsMap.computeIfAbsent(id, key -> new PlayerSettings());
    }
}
