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

import java.util.concurrent.CompletableFuture;

public class JoinListener implements Listener {

    private final DJoin plugin;

    public JoinListener(DJoin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Disable default join msg
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        // ASync LP User
        CompletableFuture<User> userFuture = plugin.getLuckPerms().getUserManager().loadUser(player.getUniqueId());
        userFuture.thenAcceptAsync(user -> {
            String primaryGroup = user.getPrimaryGroup();
            String rawMessage = plugin.getGroupMessage(primaryGroup);

            // Try default
            if (rawMessage == null || rawMessage.isEmpty()) {
                rawMessage = plugin.getGroupMessage("default");
                if (rawMessage == null || rawMessage.isEmpty()) {
                    return;
                }
            }

            // Use PlaceholderAPI
            String parsed = plugin.setPlaceholders(player, rawMessage);

            // Convert Colors and HEX to Component
            Component messageComponent = ColorUtil.parse(parsed);

            Bukkit.getScheduler().runTask(plugin, () -> {
                Bukkit.getServer().broadcast(messageComponent);
            });
        }).exceptionally(ex -> {
            plugin.getLogger().warning("Can't get user's group " + player.getName() + ": " + ex.getMessage());
            return null;
        });
    }
}