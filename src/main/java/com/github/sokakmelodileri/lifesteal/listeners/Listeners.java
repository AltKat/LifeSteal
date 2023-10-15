package com.github.sokakmelodileri.lifesteal.listeners;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.security.Signature;
import java.sql.SQLException;

public class Listeners implements Listener {
    LifeSteal plugin;
    public Listeners(LifeSteal plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        Player killer = event.getEntity().getKiller();
        if(plugin.getConfig().getBoolean("lose-health-only-on-player-kill")){
            if(killer == null) return;
        }

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
                plugin.getHealthsDatabase().setBanned(player);
            }
        }
        if(killer != null){
            plugin.getHealthsDatabase().updateHealth(killer, plugin.getHealthsDatabase().getPlayerHealths(killer)+1);
            plugin.sendMessage(killer, "kill", player.getName(),String.valueOf(plugin.getHealthsDatabase().getPlayerHealths(killer)));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        if(plugin.getConfig().getString("actionbar-health").equalsIgnoreCase("true")) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                int healths = 0;
                healths = plugin.getHealthsDatabase().getPlayerHealths(event.getPlayer());
                String healthString = String.valueOf(healths);
                event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText((plugin.getConfig().getString("action-bar-text")).replace("%health%", healthString).replace("&", "§")));
            }, 0, 40);
        }
        if(plugin.getHealthsDatabase().getPlayerHealths(event.getPlayer()) <= 0){
            plugin.getHealthsDatabase().updateHealth(event.getPlayer(), plugin.getConfig().getInt("starting-health"));
        }
        plugin.getHealthsDatabase().setUnBanned(event.getPlayer().getName());
        if(plugin.getHealthsDatabase().playerExists(event.getPlayer())) return;
        //plugin.getHealthsDatabase().removePlayer(event.getPlayer().getUniqueId().toString());
        plugin.getHealthsDatabase().addPlayer(event.getPlayer().getUniqueId().toString(), event.getPlayer().getName());
        plugin.getHealthsDatabase().updateHealth(event.getPlayer(), plugin.getConfig().getInt("starting-health"));
    }

    @EventHandler
    public void OnPaperUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType().equals(Material.PAPER) && item.getItemMeta().getEnchants().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL) && item.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ENCHANTS)){
            plugin.getHealthsDatabase().updateHealth(player, plugin.getHealthsDatabase().getPlayerHealths(player)+1);
            plugin.sendMessage(player, "paper-used", String.valueOf(plugin.getHealthsDatabase().getPlayerHealths(player)));
            item.setAmount(item.getAmount()-1);
        }
    }

    @EventHandler
    public void OnStickUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        boolean oncooldown = plugin.cooldowns.getOrDefault(player.getUniqueId(), false);
        if(item.getType().equals(Material.STICK) && item.getItemMeta().getEnchants().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL) && item.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ENCHANTS)){
        //opens a sign for the player
            if(oncooldown) {
                plugin.sendMessage(player, "cooldown");
                return;
            }
            plugin.sendMessage(player, "stick-used");
            plugin.cooldowns.put(player.getUniqueId(), true);
        }
    }

    @EventHandler
    public void OnPlayerChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Boolean oncooldown = plugin.cooldowns.getOrDefault(player.getUniqueId(), false);
        if(oncooldown) {
            if (item.getType().equals(Material.STICK) && item.getItemMeta().getEnchants().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL) && item.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ENCHANTS)) {
                String nick = event.getMessage();
                if (plugin.getHealthsDatabase().checkBanned(nick)) {
                    event.setCancelled(true);
                    String unbanReason = plugin.getConfig().getString("unban-reason");
                    String unbanCommand = "unban " + nick + " " + unbanReason;
                    player.getInventory().getItemInMainHand().setAmount(item.getAmount() - 1);
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), unbanCommand);
                    plugin.getHealthsDatabase().setUnBanned(nick);
                    plugin.getHealthsDatabase().updateHealthbyUsername(nick, plugin.getConfig().getInt("starting-health"));
                    if(plugin.getConfig().getBoolean("unban-stick-broadcast-enabled")) {
                        plugin.getServer().broadcastMessage(plugin.pluginTag + plugin.getConfig().getString("messages.unban-stick-broadcast").replace("%player%", nick).replace("%executor%", player.getName()).replace("&", "§"));
                    }

                } else {
                    event.setCancelled(true);
                    plugin.sendMessage(player, "player_not_banned");
                }

            }
            plugin.cooldowns.put(player.getUniqueId(), false);
        }

    }
}
