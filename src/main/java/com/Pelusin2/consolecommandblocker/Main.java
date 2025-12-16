package com.Pelusin2.consolecommandblocker;

import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
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

        if (isSpanish) {
            getLogger().info("Console Command Blocker cargado. Idioma detectado: Español.");
        } else {
            getLogger().info("Console Command Blocker loaded. Detected language: " + Locale.getDefault().getLanguage());
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ccblocker")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                loadConfigData();

                if (isSpanish) {
                    sender.sendMessage("§aCCBlocker: Configuración recargada correctamente.");
                } else {
                    sender.sendMessage("§aCCBlocker: Configuration reloaded successfully.");
                }
                return true;
            } else {
                if (isSpanish) {
                    sender.sendMessage("§cUso: /CCBlocker reload");
                } else {
                    sender.sendMessage("§cUsage: /CCBlocker reload");
                }
                return true;
            }
        }
        return false;
    }
}
