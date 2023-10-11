package com.github.sokakmelodileri.lifesteal;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class DeathListener implements Listener {
    LifeSteal plugin;
    DeathListener(LifeSteal plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) throws SQLException {
        Player player = event.getEntity().getPlayer();
        if(player != null){
            plugin.getHealthsDatabase().updateHealth(player, plugin.getHealthsDatabase().getPlayerHealths(player)-1);
            int healths = plugin.getHealthsDatabase().getPlayerHealths(player);
            if(healths > 0) {
                plugin.sendMessage(player, "death", String.valueOf(healths));
            }

            healths = plugin.getHealthsDatabase().getPlayerHealths(player);
            String banDuration = plugin.getConfig().getString("ban-duration");
            String banReason = plugin.getConfig().getString("ban-reason");
            String banCommand = "tempban " + player.getName() + " " + banDuration + " " + banReason;
            if(healths == 0){
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), banCommand);
                    if(plugin.getConfig().getString("special-ban-message").equalsIgnoreCase("true")) {
                        Bukkit.broadcastMessage(plugin.pluginTag + plugin.getConfig().getString("messages.banned").replace("%player%", player.getName()).replace("&", "§"));
                    }
                plugin.getHealthsDatabase().removePlayer(player.getUniqueId().toString());
            }
        }
        Player killer = event.getEntity().getKiller();
        if(killer instanceof Player){
            plugin.getHealthsDatabase().updateHealth(killer, plugin.getHealthsDatabase().getPlayerHealths(killer)+1);
            plugin.sendMessage(killer, "kill", player.getName(),String.valueOf(plugin.getHealthsDatabase().getPlayerHealths(killer)));
        }
    }

    @EventHandler
    public void onPlayerFirstJoin(PlayerJoinEvent event) throws SQLException {
        if(plugin.getHealthsDatabase().playerExists(event.getPlayer())) return;
        try {
            plugin.getHealthsDatabase().addPlayer(event.getPlayer().getUniqueId().toString(), event.getPlayer().getName());
            plugin.getHealthsDatabase().updateHealth(event.getPlayer(), 5);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
