package me.rayzr522.sunshine.command;

import me.rayzr522.sunshine.Sunshine;
import me.rayzr522.sunshine.struct.PlayerSettings;
import me.rayzr522.sunshine.utils.FormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandSunshine implements CommandExecutor {
    private final Sunshine plugin;

    public CommandSunshine(Sunshine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.checkPermission(sender, "command.use", true)) {
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.tr("command.fail.only-players"));
            return true;
        }

        Player player = (Player) sender;
        PlayerSettings settings = plugin.getPlayerSettingsManager().getPlayerSettings(player.getUniqueId());

        if (args.length < 1) {
            showUsage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "version":
                sender.sendMessage(plugin.tr("command.sunshine.version", plugin.getDescription().getVersion()));
                break;
            case "reload":
                if (!isAdmin(sender)) {
                    break;
                }

                plugin.reload();
                sender.sendMessage(plugin.tr("command.sunshine.reloaded"));
                break;
            case "info":
                sender.sendMessage(plugin.trRaw(
                        "command.sunshine.info",
                        plugin.trRaw(String.format("system.constants.%s", settings.isEnabled() ? "enabled" : "disabled")),
                        FormatUtils.time(settings.getPlaytimeLimit()),
                        FormatUtils.time(settings.getRejoinDelay()),
                        FormatUtils.time(settings.getWarningBufferTime()),
                        settings.getWarningMessage().orElse(plugin.trRaw("system.constants.none"))
                ));
                break;
            case "enable":
                settings.setEnabled(true);
                plugin.save();
                sender.sendMessage(plugin.tr("command.sunshine.enabled"));
                break;
            case "disable":
                settings.setEnabled(false);
                plugin.save();
                sender.sendMessage(plugin.tr("command.sunshine.disabled"));
                break;
            case "playtime-limit":
                if (args.length < 2) {
                    sender.sendMessage(plugin.tr("command.sunshine.playtime-limit.usage"));
                    break;
                }

                int playtimeLimit = -1;
                try {
                    playtimeLimit = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignore) {
                    sender.sendMessage(plugin.tr("command.fail.not-number", args[1]));
                    break;
                }

                if (playtimeLimit < 1) {
                    sender.sendMessage(plugin.tr("command.sunshine.playtime-limit.invalid"));
                    break;
                }

                settings.setPlaytimeLimit(playtimeLimit);
                plugin.save();
                sender.sendMessage(plugin.tr("command.sunshine.playtime-limit.set", FormatUtils.time(playtimeLimit)));
                break;
            case "warning-buffer-time":
                if (args.length < 2) {
                    sender.sendMessage(plugin.tr("command.sunshine.warning-buffer-time.usage"));
                    break;
                }

                int warningBufferTime = -1;
                try {
                    warningBufferTime = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignore) {
                    sender.sendMessage(plugin.tr("command.fail.not-number", args[1]));
                    break;
                }

                if (warningBufferTime < 0) {
                    sender.sendMessage(plugin.tr("command.sunshine.warning-buffer-time.invalid"));
                    break;
                }

                settings.setWarningBufferTime(warningBufferTime);
                plugin.save();
                sender.sendMessage(plugin.tr("command.sunshine.warning-buffer-time.set", FormatUtils.time(warningBufferTime)));
                break;
            case "rejoin-delay":
                if (args.length < 2) {
                    sender.sendMessage(plugin.tr("command.sunshine.rejoin-delay.usage"));
                    break;
                }

                int rejoinDelay = -1;
                try {
                    rejoinDelay = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignore) {
                    sender.sendMessage(plugin.tr("command.fail.not-number", args[1]));
                    break;
                }

                if (rejoinDelay < 0) {
                    sender.sendMessage(plugin.tr("command.sunshine.rejoin-delay.invalid"));
                    break;
                }

                settings.setRejoinDelay(rejoinDelay);
                plugin.save();
                sender.sendMessage(plugin.tr("command.sunshine.rejoin-delay.set", FormatUtils.time(rejoinDelay)));
                break;
            case "warning-message":
                if (args.length < 2) {
                    settings.setWarningMessage(null);
                    sender.sendMessage(plugin.tr("command.sunshine.warning-message.removed"));
                } else {
                    String warningMessage = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    settings.setWarningMessage(warningMessage);
                    sender.sendMessage(plugin.tr("command.sunshine.warning-message.set", ChatColor.translateAlternateColorCodes('&', warningMessage)));
                }
                plugin.save();
                break;
            default:
                showUsage(sender);
        }

        return true;
    }

    private boolean isAdmin(CommandSender sender) {
        return plugin.checkPermission(sender, "command.admin", true);
    }

    private void showUsage(CommandSender sender) {
        sender.sendMessage(plugin.trRaw("command.sunshine.usage"));
    }
}
