package com.github.sokakmelodileri.lifesteal;

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
        sender.sendMessage("§cWrong Usage should be §a/sethealth <player> <amount>");
        }


        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if(target == null){
            sender.sendMessage("§4Player not found!");
            return true;
        }
        int amount = 0;
        try {
            amount = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            sender.sendMessage("§cInvalid amount has provided please provide a whole number.");
            return true;
        }
        try {
            plugin.getHealthsDatabase().updateHealth(target, amount);
            sender.sendMessage("§aSuccessfully updated §b" + target.getName() + "§a's healths to §a" + amount + "§a!");
        }catch (SQLException e){
            sender.sendMessage("§cAn error occured while updating the player's healths.");
        }

        return true;
    }

}
