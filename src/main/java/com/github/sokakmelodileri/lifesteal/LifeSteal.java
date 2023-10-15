package com.github.sokakmelodileri.lifesteal;

import com.github.sokakmelodileri.lifesteal.commands.*;
import com.github.sokakmelodileri.lifesteal.database.Database;
import com.github.sokakmelodileri.lifesteal.listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public final class LifeSteal extends JavaPlugin {
    private Database db;
    FileConfiguration config = getConfig();
    public String pluginTag = (config.getString("pluginTag") + "§r ").replace("&", "§");
    @Override
    public void onEnable() {
        // Plugin startup logic
        initDB();
        configYenile();
        getCommand("sethealth").setExecutor(new SetHealth(this));
        getCommand("gethealth").setExecutor(new GetHealth(this));
        getCommand("lifesteal").setExecutor(new MainCommands(this));
        getCommand("withdraw").setExecutor(new WithdrawCommand(this));
        getCommand("lifesteal").setTabCompleter(new TabComplete(this));
        getServer().getPluginManager().registerEvents(new Listeners(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        closeDB();
    }

    public void configYenile(){
        reloadConfig();
        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
    }

    public void initDB(){
        try {
            if(!getDataFolder().exists()) getDataFolder().mkdir();
            File dbFile = new File(getDataFolder(), "database.db");
            if (!dbFile.exists()) dbFile.createNewFile();
            db = new Database(dbFile.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("§4Failed to connect to do database! Plugin shutting down...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void closeDB(){
        try {
            db.closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Database getHealthsDatabase(){
        return this.db;
    }

    public void sendMessage(CommandSender receiver, String path, String... args){
        String rawMessage = getConfig().getString("messages."+path);
        rawMessage = rawMessage.replace("&", "§");
        String formattedMessage = String.format(rawMessage, (Object[]) args);
        receiver.sendMessage(pluginTag + formattedMessage);
    }



}
