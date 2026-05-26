package me.demro.dJoin.commands;

import me.demro.dJoin.DJoin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
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
                    sender.sendMessage(plugin.getPrefixedMessage("usage_addgroup", null));
                    return true;
                }
                plugin.addGroup(args[1]);
                sender.sendMessage(plugin.getPrefixedMessage("group_added", Map.of("group", args[1])));
                break;

            case "delgroup":
                if (args.length != 2) {
                    sender.sendMessage(plugin.getPrefixedMessage("usage_delgroup", null));
                    return true;
                }
                if (plugin.getGroupMessage(args[1]) == null) {
                    sender.sendMessage(plugin.getPrefixedMessage("group_not_found", Map.of("group", args[1])));
                    return true;
                }
                plugin.removeGroup(args[1]);
                sender.sendMessage(plugin.getPrefixedMessage("group_removed", Map.of("group", args[1])));
                break;

            case "setmsg":
                if (args.length < 3) {
                    sender.sendMessage(plugin.getPrefixedMessage("usage_setmsg", null));
                    return true;
                }
                String groupMsg = args[1];
                String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                if (message.startsWith("\"") && message.endsWith("\"")) {
                    message = message.substring(1, message.length() - 1);
                }
                plugin.setGroupMessage(groupMsg, message);
                sender.sendMessage(plugin.getPrefixedMessage("msg_set", Map.of("group", groupMsg)));
                break;

            case "addcmd":
                if (args.length < 3) {
                    sender.sendMessage(plugin.getPrefixedMessage("usage_addcmd", null));
                    return true;
                }
                String groupAdd = args[1];
                String cmd = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                if (cmd.startsWith("\"") && cmd.endsWith("\"")) {
                    cmd = cmd.substring(1, cmd.length() - 1);
                }
                plugin.addGroupCommand(groupAdd, cmd);
                sender.sendMessage(plugin.getPrefixedMessage("cmd_added", Map.of("group", groupAdd)));
                break;

            case "delcmd":
                if (args.length != 3) {
                    sender.sendMessage(plugin.getPrefixedMessage("usage_delcmd", null));
                    return true;
                }
                String groupDel = args[1];
                try {
                    int index = Integer.parseInt(args[2]);
                    if (plugin.removeGroupCommand(groupDel, index)) {
                        sender.sendMessage(plugin.getPrefixedMessage("cmd_removed", Map.of("group", groupDel, "index", String.valueOf(index))));
                    } else {
                        sender.sendMessage(plugin.getPrefixedMessage("cmd_invalid_index", null));
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(plugin.getPrefixedMessage("cmd_invalid_index", null));
                }
                break;

            case "listcmd":
                if (args.length != 2) {
                    sender.sendMessage(plugin.getPrefixedMessage("usage_listcmd", null));
                    return true;
                }
                String groupList = args[1];
                List<String> cmds = plugin.listGroupCommands(groupList);
                if (cmds.isEmpty()) {
                    sender.sendMessage(plugin.getPrefixedMessage("cmd_list_empty", Map.of("group", groupList)));
                } else {
                    sender.sendMessage(plugin.getPrefixedMessage("cmd_list_header", Map.of("group", groupList)));
                    for (int i = 0; i < cmds.size(); i++) {
                        sender.sendMessage(plugin.getPrefixedMessage("cmd_list_entry", Map.of("index", String.valueOf(i), "command", cmds.get(i))));
                    }
                }
                break;

            case "reload":
                plugin.reload();
                sender.sendMessage(plugin.getPrefixedMessage("reload_success", null));
                break;

            default:
                sendHelp(sender);
                break;
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(plugin.getPrefixedMessage("help_header", null));
        sender.sendMessage(plugin.getPrefixedMessage("help_addgroup", null));
        sender.sendMessage(plugin.getPrefixedMessage("help_delgroup", null));
        sender.sendMessage(plugin.getPrefixedMessage("help_setmsg", null));
        sender.sendMessage(plugin.getPrefixedMessage("help_addcmd", null));
        sender.sendMessage(plugin.getPrefixedMessage("help_delcmd", null));
        sender.sendMessage(plugin.getPrefixedMessage("help_listcmd", null));
        sender.sendMessage(plugin.getPrefixedMessage("help_reload", null));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("addgroup", "delgroup", "setmsg", "addcmd", "delcmd", "listcmd", "reload")
                    .stream().filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("delgroup") || args[0].equalsIgnoreCase("setmsg") ||
                args[0].equalsIgnoreCase("addcmd") || args[0].equalsIgnoreCase("delcmd") || args[0].equalsIgnoreCase("listcmd"))) {
            return plugin.getConfig().getConfigurationSection("join-messages").getKeys(false).stream()
                    .filter(g -> g.startsWith(args[1]))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}