package me.rayzr522.sunshine.command;

import me.rayzr522.sunshine.Sunshine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
                    return true;
                }

                plugin.reload();
                sender.sendMessage(plugin.tr("command.sunshine.reloaded"));
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
