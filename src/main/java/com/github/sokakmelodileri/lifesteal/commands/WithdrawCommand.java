package com.github.sokakmelodileri.lifesteal.commands;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import com.github.sokakmelodileri.lifesteal.items.HealthPaper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class WithdrawCommand extends HealthPaper implements CommandExecutor {
    LifeSteal plugin;
    public WithdrawCommand(LifeSteal plugin){
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 1){
            plugin.sendMessage(sender, "withdraw-usage");
            return true;
        }

        if(args.length == 0 && sender instanceof Player){
            Player player = ((Player) sender).getPlayer();
            if(sender.hasPermission("lifesteal.withdraw")) {
                try {
                    if(plugin.getHealthsDatabase().getPlayerHealths(player) > 1) {
                        ItemStack item = getItem(1);
                        int amount = 1;
                        int remainingHealths;
                        player.getInventory().addItem(item);

                            plugin.getHealthsDatabase().updateHealth(player, plugin.getHealthsDatabase().getPlayerHealths(player) - 1);
                        try {
                            remainingHealths = plugin.getHealthsDatabase().getPlayerHealths(player);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        plugin.sendMessage(player, "withdraw-succeed", String.valueOf(amount), String.valueOf(remainingHealths));
                    }else {
                        plugin.sendMessage(player, "not-enough-healths");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                plugin.sendMessage(sender,"no_permission");
            }
            return true;
        }
        if(args.length == 1 && sender instanceof Player){
            Player player = ((Player) sender).getPlayer();
            int amount;
            try {
                amount = Integer.parseInt(args[0]);
                int remainingHealths;
                ItemStack item = getItem(amount);
                if(sender.hasPermission("lifesteal.withdraw")) {
                    try {
                        if(plugin.getHealthsDatabase().getPlayerHealths(player) > 1 && amount < plugin.getHealthsDatabase().getPlayerHealths(player)){
                            player.getInventory().addItem(item);
                            plugin.getHealthsDatabase().updateHealth(player, plugin.getHealthsDatabase().getPlayerHealths(player) - amount);
                            remainingHealths = plugin.getHealthsDatabase().getPlayerHealths(player);

                            plugin.sendMessage(player, "withdraw-succeed", String.valueOf(amount), String.valueOf(remainingHealths));
                        }else {
                            plugin.sendMessage(player, "not-enough-healths");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    plugin.sendMessage(sender,"no_permission");
                }
            }catch (NumberFormatException e){
                plugin.sendMessage(player, "sethealth.wrongnumber");
            }
            return true;
        }
        return true;
    }
}
