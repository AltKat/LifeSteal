package com.github.sokakmelodileri.lifesteal;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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
            player.sendMessage("§cYou have lost §e1 §chealth because you have died. §aYou have remaining §b" + healths + " §ahealths. §4If you run out of healths you will get banned from the server for 1 day and your character will get wiped!");

            healths = plugin.getHealthsDatabase().getPlayerHealths(player);
            if(healths == 0){
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getUniqueId().toString(), "Run out of healths", null, "console");
                event.getEntity().getPlayer().kickPlayer("§cYou have run out of healths and get banned from the server for 1 day and your character has been wiped!");
                Bukkit.broadcastMessage("§e" + player.getName() + " §cGet banned from the server because it run out of healths!");
            }


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
