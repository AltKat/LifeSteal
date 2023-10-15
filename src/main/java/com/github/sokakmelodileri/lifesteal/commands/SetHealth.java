package com.github.sokakmelodileri.lifesteal.commands;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class SetHealth implements CommandExecutor {
    private final LifeSteal plugin;
    public SetHealth(LifeSteal plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length != 2) {
        plugin.sendMessage(sender, "sethealth.usage");
        return true;
        }


        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if(target == null){
            plugin.sendMessage(sender, "player_not_found");
            return true;
        }
        int amount = 0;
        try {
            amount = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            plugin.sendMessage(sender, "sethealth.wrongnumber");
            return true;
        }
        plugin.getHealthsDatabase().updateHealth(target, amount);
        plugin.sendMessage(sender, "sethealth.success", target.getName() , String.valueOf(amount));

        return true;
    }

}
