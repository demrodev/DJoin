package me.demro.dJoin.commands;

import me.demro.dJoin.DJoin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JoinCommand implements CommandExecutor, TabCompleter {

    private final DJoin plugin;

    public JoinCommand(DJoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "addgroup":
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Использование: /join addgroup <название_группы>");
                    return true;
                }
                plugin.addGroup(args[1]);
                sender.sendMessage(ChatColor.GREEN + "Группа '" + args[1] + "' добавлена в конфиг. Не забудьте установить сообщение через /join setmsg.");
                break;

            case "delgroup":
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Использование: /join delgroup <название_группы>");
                    return true;
                }
                plugin.removeGroup(args[1]);
                sender.sendMessage(ChatColor.GREEN + "Группа '" + args[1] + "' удалена из конфига.");
                break;

            case "setmsg":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Использование: /join setmsg <название_группы> \"<сообщение>\"");
                    return true;
                }
                String group = args[1];
                String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                if (message.startsWith("\"") && message.endsWith("\"")) {
                    message = message.substring(1, message.length() - 1);
                }
                plugin.setGroupMessage(group, message);
                sender.sendMessage(ChatColor.GREEN + "Сообщение для группы '" + group + "' установлено.");
                break;

            case "reload":
                plugin.reload();
                sender.sendMessage(ChatColor.GREEN + "Конфигурация перезагружена.");
                break;

            default:
                sendHelp(sender);
                break;
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "Команды DJoin:");
        sender.sendMessage(ChatColor.YELLOW + "/join addgroup <группа> " + ChatColor.WHITE + " - добавить группу в конфиг");
        sender.sendMessage(ChatColor.YELLOW + "/join delgroup <группа> " + ChatColor.WHITE + " - удалить группу из конфига");
        sender.sendMessage(ChatColor.YELLOW + "/join setmsg <группа> \"сообщение\" " + ChatColor.WHITE + " - установить сообщение для группы");
        sender.sendMessage(ChatColor.YELLOW + "/join reload " + ChatColor.WHITE + " - перезагрузить конфиг");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("addgroup", "delgroup", "setmsg", "reload").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("delgroup") || args[0].equalsIgnoreCase("setmsg"))) {
            // Предлагаем группы из конфига
            return plugin.getConfig().getConfigurationSection("join-messages").getKeys(false).stream()
                    .filter(g -> g.startsWith(args[1]))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}