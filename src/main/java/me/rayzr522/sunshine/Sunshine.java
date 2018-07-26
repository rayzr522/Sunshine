package me.rayzr522.sunshine;

import me.rayzr522.sunshine.data.PlayerSettingsManager;
import me.rayzr522.sunshine.data.Settings;
import me.rayzr522.sunshine.event.PlayerListener;
import me.rayzr522.sunshine.task.PlaytimeCheckTask;
import me.rayzr522.sunshine.utils.MessageHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * @author Rayzr
 */
public class Sunshine extends JavaPlugin {
    private static Sunshine instance;

    private final Settings settings = new Settings();
    private final MessageHandler messages = new MessageHandler();
    private final PlayerSettingsManager playerSettingsManager = new PlayerSettingsManager();

    private PlaytimeCheckTask playtimeCheckTask;

    public static Sunshine getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        reload();

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // TODO: Command registration.
    }

    @Override
    public void onDisable() {
        instance = null;

        save();
    }

    /**
     * (Re)loads all configs from the disk
     */
    public void reload() {
        saveDefaultConfig();
        reloadConfig();

        // Load configs
        settings.load(getConfig());
        messages.load(getConfig("messages.yml"));
        playerSettingsManager.load(getConfig("players.yml"));

        // Setup playtime check task
        if (playtimeCheckTask != null) {
            playtimeCheckTask.cancel();
        }

        playtimeCheckTask = new PlaytimeCheckTask(this);
        playtimeCheckTask.runTaskTimer(this, 0L, settings.getCheckDelay());
    }

    /**
     * Saves all persistent data to the disk
     */
    public void save() {
        saveConfig(playerSettingsManager.save(), "players.yml");
    }

    /**
     * If the file is not found and there is a default file in the JAR, it saves the default file to the plugin data folder first
     *
     * @param path The path to the config file (relative to the plugin data folder)
     * @return The {@link YamlConfiguration}
     */
    public YamlConfiguration getConfig(String path) {
        if (!getFile(path).exists() && getResource(path) != null) {
            saveResource(path, true);
        }
        return YamlConfiguration.loadConfiguration(getFile(path));
    }

    /**
     * Attempts to save a {@link YamlConfiguration} to the disk, and any {@link IOException}s are printed to the console
     *
     * @param config The config to save
     * @param path   The path to save the config file to (relative to the plugin data folder)
     */
    public void saveConfig(YamlConfiguration config, String path) {
        try {
            config.save(getFile(path));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to save config", e);
        }
    }

    /**
     * @param path The path of the file (relative to the plugin data folder)
     * @return The {@link File}
     */
    public File getFile(String path) {
        return new File(getDataFolder(), path.replace('/', File.separatorChar));
    }

    /**
     * Returns a message from the language file
     *
     * @param key     The key of the message to translate
     * @param objects The formatting objects to use
     * @return The formatted message
     */
    public String tr(String key, Object... objects) {
        return messages.tr(key, objects);
    }

    /**
     * Returns a message from the language file without adding the prefix
     *
     * @param key     The key of the message to translate
     * @param objects The formatting objects to use
     * @return The formatted message
     */
    public String trRaw(String key, Object... objects) {
        return messages.trRaw(key, objects);
    }

    /**
     * Checks a target {@link CommandSender} for a given permission (excluding the permission base). Example:
     * <p>
     * <pre>
     *     checkPermission(sender, "command.use", true);
     * </pre>
     * <p>
     * This would check if the player had the permission <code>"{plugin name}.command.use"</code>, and if they didn't, it would send them the no-permission message from the messages config file.
     *
     * @param target      The target {@link CommandSender} to check
     * @param permission  The permission to check, excluding the permission base (which is the plugin name)
     * @param sendMessage Whether or not to send a no-permission message to the target
     * @return Whether or not the target has the given permission
     */
    public boolean checkPermission(CommandSender target, String permission, boolean sendMessage) {
        String fullPermission = String.format("%s.%s", getName(), permission);

        if (!target.hasPermission(fullPermission)) {
            if (sendMessage) {
                target.sendMessage(tr("no-permission", fullPermission));
            }

            return false;
        }

        return true;
    }

    /**
     * @return The settings for this plugin
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * @return The per-player settings manager
     */
    public PlayerSettingsManager getPlayerSettingsManager() {
        return playerSettingsManager;
    }
}
