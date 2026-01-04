package com.Pelusin2.consolecommandblocker;

import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private List<String> blockedCommands;
    private boolean isSpanish;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigData();

        Bukkit.getPluginManager().registerEvents(this, this);

        registerCommands(); // <-- importante

        if (isSpanish) {
            getLogger().info("Console Command Blocker cargado. Idioma detectado: Español.");
        } else {
            getLogger().info("Console Command Blocker loaded. Detected language: " + Locale.getDefault().getLanguage());
        }
    }

    private void registerCommands() {
        var command = new org.bukkit.command.Command("ccblocker") {
            @Override
            public boolean execute(CommandSender sender, String label, String[] args) {
                return onCommand(sender, this, label, args);
            }
        };

        try {
            var commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            var commandMap = (org.bukkit.command.CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register("ccblocker", command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadConfigData() {
        blockedCommands = getConfig().getStringList("Blocked_commands");
        String lang = Locale.getDefault().getLanguage();
        isSpanish = lang.equalsIgnoreCase("es");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsoleCommand(ServerCommandEvent event) {
        String cmd = event.getCommand().toLowerCase();

        for (String blocked : blockedCommands) {
            if (cmd.startsWith(blocked.toLowerCase())) {
                event.setCancelled(true);

                if (isSpanish) {
                    Bukkit.getLogger().warning("La consola NO puede usar /" + blocked);
                } else {
                    Bukkit.getLogger().warning("The console cannot use /" + blocked);
                }

                return;
            }
        }
    }
}
