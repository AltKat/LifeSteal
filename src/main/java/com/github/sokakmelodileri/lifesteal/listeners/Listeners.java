package com.github.sokakmelodileri.lifesteal.listeners;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class Listeners implements Listener {
    LifeSteal plugin;
    public Listeners(LifeSteal plugin){
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
            if(healths <= 0){
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
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        if(plugin.getConfig().getString("actionbar-health").equalsIgnoreCase("true")) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                int healths = 0;
                try {
                    healths = plugin.getHealthsDatabase().getPlayerHealths(event.getPlayer());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                String healthString = String.valueOf(healths);
                event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText((plugin.getConfig().getString("action-bar-text")).replace("%health%", healthString).replace("&", "§")));
            }, 0, 40);
        }

        if(plugin.getHealthsDatabase().playerExists(event.getPlayer())) return;
        try {
            plugin.getHealthsDatabase().addPlayer(event.getPlayer().getUniqueId().toString(), event.getPlayer().getName());
            plugin.getHealthsDatabase().updateHealth(event.getPlayer(), 5);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @EventHandler
    public void OnPaperUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType().equals(Material.PAPER) && item.getItemMeta().getEnchants().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL) && item.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ENCHANTS)){
            try {
                plugin.getHealthsDatabase().updateHealth(player, plugin.getHealthsDatabase().getPlayerHealths(player)+1);
                plugin.sendMessage(player, "paper-used", String.valueOf(plugin.getHealthsDatabase().getPlayerHealths(player)));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            item.setAmount(item.getAmount()-1);
        }
    }
}
