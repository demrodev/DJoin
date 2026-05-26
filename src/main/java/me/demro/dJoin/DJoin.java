package me.demro.dJoin;

import me.demro.dJoin.commands.JoinCommand;
import me.demro.dJoin.listeners.JoinListener;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DJoin extends JavaPlugin {

    private static DJoin instance;
    private LuckPerms luckPerms;
    private boolean placeholderAPIEnabled;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();

        // Initial LP
        try {
            luckPerms = LuckPermsProvider.get();
            getLogger().info("LuckPerms API is loaded.");
        } catch (Exception e) {
            getLogger().severe("Plugin needs LuckPerms, install it.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Check PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderAPIEnabled = true;
            getLogger().info("PlaceholderAPI is loaded.");
        } else {
            placeholderAPIEnabled = false;
            getLogger().warning("The plugin needs a PlaceholderAPI, placeholders will not work, install the plugin.");
        }

        // Command Register
        getCommand("join").setExecutor(new JoinCommand(this));
        getCommand("join").setTabCompleter(new JoinCommand(this));

        // Listener Register
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        getLogger().info("Plugin is enabled. Plugin by DemRoDev. https://t.me/notromanenko");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin is disabled");
    }

    public static DJoin getInstance() {
        return instance;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public boolean isPlaceholderAPIEnabled() {
        return placeholderAPIEnabled;
    }

    // Config

    public String getGroupMessage(String group) {
        return getConfig().getString("join-messages." + group);
    }

    public void setGroupMessage(String group, String message) {
        getConfig().set("join-messages." + group, message);
        saveConfig();
    }

    public void addGroup(String group) {
        if (!getConfig().contains("join-messages." + group)) {
            getConfig().set("join-messages." + group, "");
            saveConfig();
        }
    }

    public void removeGroup(String group) {
        if (getConfig().contains("join-messages." + group)) {
            getConfig().set("join-messages." + group, null);
            saveConfig();
        }
    }

    public void reload() {
        reloadConfig();
    }

    // Processing Placeholders
    public String setPlaceholders(org.bukkit.entity.Player player, String text) {
        if (placeholderAPIEnabled) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }
}