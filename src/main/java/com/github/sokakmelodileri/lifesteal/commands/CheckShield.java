package com.github.sokakmelodileri.lifesteal.commands;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckShield implements CommandExecutor {
    LifeSteal plugin;
    public CheckShield(LifeSteal plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = ((Player) sender).getPlayer();
        if(args.length == 0){
            if(plugin.getHealthsDatabase().checkShield(player)){
                plugin.sendMessage(sender, "checkshield-true", player.getName());
            }else{
                plugin.sendMessage(sender, "checkshield-false", player.getName());
            }
            return true;
        }
        if(args.length == 1) {
            if (sender.hasPermission("lifesteal.admin")) {
                String playerName = args[0];
                Player target = plugin.getServer().getPlayer(playerName);
                if (target == null) {
                    plugin.sendMessage(sender, "player_not_found");
                    return true;
                }
                if (plugin.getHealthsDatabase().checkShield(target)) {
                    plugin.sendMessage(sender, "checkshield-true", target.getName());
                } else {
                    plugin.sendMessage(sender, "checkshield-false", target.getName());
                }
                return true;
            }else{
                plugin.sendMessage(sender, "no_permission");
            }
        }
        if (args.length > 1) {
            if (sender.hasPermission("lifesteal.admin")) {
                plugin.sendMessage(sender, "checkshield-admin-usage");
            } else {
                plugin.sendMessage(sender, "checkshield-usage");
            }
            return true;
        }
        return true;
    }
}
