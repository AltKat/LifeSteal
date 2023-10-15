package com.github.sokakmelodileri.lifesteal.commands;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommands implements CommandExecutor {
    LifeSteal plugin;
    public MainCommands(LifeSteal plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            plugin.sendMessage(sender, "help");
            return true;
        }
        if(args[0].equals("reload")){
            plugin.configYenile();
            plugin.sendMessage(sender, "reload");
            return true;
        }
        if(args[0].equals("help")){
            if(sender.hasPermission("lifesteal.admin")){
                plugin.sendMessage(sender, "helpheader");
                plugin.sendMessage(sender, "helpadmincontent1");
                plugin.sendMessage(sender, "helpadmincontent2");
                plugin.sendMessage(sender, "helpcontent1");
                plugin.sendMessage(sender, "helpcontent2");
                return true;
            }else {
                plugin.sendMessage(sender, "helpheader");
                plugin.sendMessage(sender, "helpcontent1");
                plugin.sendMessage(sender, "helpcontent2");
                return true;
            }

        }

        plugin.sendMessage(sender, "help");
        return true;
    }
}
