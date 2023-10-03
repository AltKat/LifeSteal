package com.github.sokakmelodileri.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class GetHealth implements CommandExecutor {
    LifeSteal plugin;
    GetHealth(LifeSteal plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
        if(args.length == 0 && sender instanceof Player player){

                int healths = plugin.getHealthsDatabase().getPlayerHealths(player);
                sender.sendMessage("§aYou have §b" + healths);
                return true;

            }else {

            String playerName = args[0];
            Player player = Bukkit.getServer().getPlayer(playerName);

            if(player == null){
                sender.sendMessage("§cPlayer not found!");
                return true;
            }

            int healths = plugin.getHealthsDatabase().getPlayerHealths(player);
            sender.sendMessage(player.getName() + " §ahas §b " + healths);
        }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return false;
    }
}
