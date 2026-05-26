package me.demro.dJoin;

import me.demro.dJoin.commands.JoinCommand;
import me.demro.dJoin.listeners.JoinListener;
import me.demro.dJoin.utils.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DJoin extends JavaPlugin {

    private static DJoin instance;
    private LuckPerms luckPerms;
    private boolean placeholderAPIEnabled;
    private Map<String, String> messageCache;
    private String prefix;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();

        // Inital LP
        try {
            luckPerms = LuckPermsProvider.get();
            getLogger().info("LuckPerms API is connected.");
        } catch (Exception e) {
            getLogger().severe("Plugin needs LuckPerms. Disabled.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Check PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderAPIEnabled = true;
            getLogger().info("PlaceholderAPI is founded, placeholders would work.");
        } else {
            placeholderAPIEnabled = false;
            getLogger().warning("PlaceholderAPI isn't founded, placeholders wouldn't work.");
        }

        // Register commands
        getCommand("join").setExecutor(new JoinCommand(this));
        getCommand("join").setTabCompleter(new JoinCommand(this));

        // Register listener
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        getLogger().info("Plugin enabled. Made with love by demro_ (https://github.com/demrodev) & (https://t.me/notromanenko");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
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

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        loadMessages();
    }

    private void loadMessages() {
        FileConfiguration config = getConfig();
        messageCache = new HashMap<>();
        // Load prefix
        prefix = config.getString("prefix", "&8[&aDJoin&8]&r");
        // Messages loading
        if (config.contains("messages")) {
            for (String key : config.getConfigurationSection("messages").getKeys(false)) {
                messageCache.put(key, config.getString("messages." + key));
            }
        }
    }

    public String getMessage(String key, Map<String, String> placeholders) {
        String msg = messageCache.getOrDefault(key, "&cMissing message: " + key);
        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                msg = msg.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return ColorUtil.parseToString(msg);
    }

    public String getPrefixedMessage(String key, Map<String, String> placeholders) {
        String msg = getMessage(key, placeholders);
        String prefixColored = ColorUtil.parseToString(prefix);
        return prefixColored + " " + msg;
    }

    // Join messages

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

    // Commands on join

    public List<String> getGroupCommands(String group) {
        return getConfig().getStringList("join-commands." + group);
    }

    public void addGroupCommand(String group, String command) {
        List<String> commands = getConfig().getStringList("join-commands." + group);
        commands.add(command);
        getConfig().set("join-commands." + group, commands);
        saveConfig();
    }

    public boolean removeGroupCommand(String group, int index) {
        List<String> commands = getConfig().getStringList("join-commands." + group);
        if (index < 0 || index >= commands.size()) return false;
        commands.remove(index);
        getConfig().set("join-commands." + group, commands);
        saveConfig();
        return true;
    }

    public List<String> listGroupCommands(String group) {
        return getConfig().getStringList("join-commands." + group);
    }

    // Cfg reload
    public void reload() {
        reloadConfig();
        loadMessages();
    }

    // Placeholders processing
    public String setPlaceholders(org.bukkit.entity.Player player, String text) {
        if (placeholderAPIEnabled) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }
}