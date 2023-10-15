package com.github.sokakmelodileri.lifesteal.commands;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class GetHealth implements CommandExecutor {
    LifeSteal plugin;
    public GetHealth(LifeSteal plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0 && sender instanceof Player player){

                int healths = plugin.getHealthsDatabase().getPlayerHealths(player);
                plugin.sendMessage(sender, "gethealth.self", String.valueOf(healths));
                return true;

            }else {
            if(sender.hasPermission("lifesteal.admin") || args[0].equals(sender.getName())){
                String playerName = args[0];
                Player player = Bukkit.getServer().getPlayer(playerName);

                if (player == null) {
                    plugin.sendMessage(sender, "player_not_found");
                    return true;
                }

                int healths = plugin.getHealthsDatabase().getPlayerHealths(player);
                plugin.sendMessage(sender, "gethealth.other", player.getName(), String.valueOf(healths));
            }else{
                plugin.sendMessage(sender, "no_permission");
            }
        }


        return true;
    }
}
