package com.github.sokakmelodileri.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public final class LifeSteal extends JavaPlugin {
    private Database db;
    @Override
    public void onEnable() {
        // Plugin startup logic
        initDB();
        getCommand("sethealth").setExecutor(new SetHealth(this));
        getCommand("gethealth").setExecutor(new GetHealth(this));
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        closeDB();
    }

    public void initDB(){
        try {
            if(!getDataFolder().exists()) getDataFolder().mkdir();
            File dbFile = new File(getDataFolder(), "database.db");
            if (!dbFile.exists()) dbFile.createNewFile();
            db = new Database(dbFile.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("Failed to connect to do database!");
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

}
