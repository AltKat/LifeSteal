package com.github.sokakmelodileri.lifesteal.commands;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import com.github.sokakmelodileri.lifesteal.items.ReviveStick;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetStick extends ReviveStick implements CommandExecutor {
    LifeSteal plugin;
    public GetStick(LifeSteal plugin){
        super(plugin);
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0 && sender instanceof Player){
            if(sender.hasPermission("lifesteal.admin")){
                Player player = ((Player) sender).getPlayer();
                player.getInventory().addItem(getStick(1));
                plugin.sendMessage(sender, "getstick-succeed");
                return true;
            }else{
                plugin.sendMessage(sender, "no_permission");
            }

        }
        if(args.length == 1 && sender instanceof Player){
            try {
                int amount = Integer.parseInt(args[0]);

                if(sender.hasPermission("lifesteal.admin")){
                    Player player = ((Player) sender).getPlayer();
                    player.getInventory().addItem(getStick(amount));
                    plugin.sendMessage(sender, "getstick-succeed");
                }else{
                    plugin.sendMessage(sender, "no_permission");
                }
            }catch (NumberFormatException e){
                plugin.sendMessage(sender, "sethealth.wrongnumber");
            }
            return true;
        }

        if(args.length == 2 && sender instanceof Player){
            try {
                int amount = Integer.parseInt(args[0]);
                String playerName = args[1];
                Player target = plugin.getServer().getPlayer(playerName);
                if(target == null){
                    plugin.sendMessage(sender, "player_not_found");
                    return true;
                }
                if(sender.hasPermission("lifesteal.admin")){
                    target.getInventory().addItem(getStick(amount));
                    plugin.sendMessage(sender, "getstick-succeed");
                }else{
                    plugin.sendMessage(sender, "no_permission");
                }
            }catch (NumberFormatException e){
                plugin.sendMessage(sender, "sethealth.wrongnumber");
            }
            return true;
        }

        if(args.length < 1){
            plugin.sendMessage(sender, "getstick-usage");
            return true;
        }

        return true;
    }
}
