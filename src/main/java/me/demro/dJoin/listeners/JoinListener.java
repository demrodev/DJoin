package me.demro.dJoin.listeners;

import me.demro.dJoin.DJoin;
import me.demro.dJoin.utils.ColorUtil;
import net.kyori.adventure.text.Component;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JoinListener implements Listener {

    private final DJoin plugin;

    public JoinListener(DJoin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        // ASync LuckPerms
        CompletableFuture<User> userFuture = plugin.getLuckPerms().getUserManager().loadUser(player.getUniqueId());
        userFuture.thenAcceptAsync(user -> {
            String primaryGroup = user.getPrimaryGroup();

            // Send join msg
            String rawMessage = plugin.getGroupMessage(primaryGroup);
            if (rawMessage == null || rawMessage.isEmpty()) {
                rawMessage = plugin.getGroupMessage("default");
            }
            if (rawMessage != null && !rawMessage.isEmpty()) {
                String parsedMsg = plugin.setPlaceholders(player, rawMessage);
                Component msgComponent = ColorUtil.parse(parsedMsg);
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().broadcast(msgComponent));
            }

            // Cmds on join
            List<String> commands = plugin.getGroupCommands(primaryGroup);
            if (commands.isEmpty()) {
                commands = plugin.getGroupCommands("default");
            }
            for (String cmd : commands) {
                String parsedCmd = plugin.setPlaceholders(player, cmd);
                final String finalCommand = parsedCmd;
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand));
            }
        }).exceptionally(ex -> {
            plugin.getLogger().warning("Ошибка получения группы для " + player.getName() + ": " + ex.getMessage());
            return null;
        });
    }
}