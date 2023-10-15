package com.github.sokakmelodileri.lifesteal.commands;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import com.github.sokakmelodileri.lifesteal.items.Shield;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetShield extends Shield implements CommandExecutor {
    LifeSteal plugin;
    public GetShield(LifeSteal plugin){
        super(plugin);
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player  = ((Player) sender).getPlayer();
        if(args.length == 0){
            if(sender.hasPermission("lifesteal.admin")){
                ItemStack item = getShield(1);
                player.getInventory().addItem(item);
                plugin.sendMessage(sender, "getshield-succeed");
                return true;
            }else{
                plugin.sendMessage(sender, "no_permission");
            }
        }
        if(args.length == 1){
            try {
                int amount = Integer.parseInt(args[0]);
                if(sender.hasPermission("lifesteal.admin")){
                    ItemStack item = getShield(amount);
                    player.getInventory().addItem(item);
                    plugin.sendMessage(sender, "getshield-succeed");
                }else{
                    plugin.sendMessage(sender, "no_permission");
                }
            }catch (NumberFormatException e){
                plugin.sendMessage(sender, "sethealth.wrongnumber");
            }
            return true;
        }
        if(args.length == 2){
            try {
                int amount = Integer.parseInt(args[0]);
                String playerName = args[1];
                Player target = plugin.getServer().getPlayer(playerName);
                if(target == null){
                    plugin.sendMessage(sender, "player_not_found");
                    return true;
                }
                if(sender.hasPermission("lifesteal.admin")){
                    ItemStack item = getShield(amount);
                    target.getInventory().addItem(item);
                    plugin.sendMessage(sender, "getshield-succeed");
                }else{
                    plugin.sendMessage(sender, "no_permission");
                }
            }catch (NumberFormatException e){
                plugin.sendMessage(sender, "sethealth.wrongnumber");
            }
            return true;
        }

        if(args.length > 2){
            plugin.sendMessage(sender, "getshield-usage");
            return true;
        }

        return false;
    }
}
