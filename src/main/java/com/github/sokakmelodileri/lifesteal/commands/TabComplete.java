package com.github.sokakmelodileri.lifesteal.commands;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {
    LifeSteal plugin;
    public TabComplete(LifeSteal plugin){
        this.plugin = plugin;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completionsPlayers = new ArrayList<>();
        completionsPlayers.add("help");

        List<String> completionsAdmin = new ArrayList<>();
        completionsAdmin.add("reload");
        completionsAdmin.add("help");

        if(sender.hasPermission("lifesteal.admin")){
            return completionsAdmin;
        }
        return completionsPlayers;
    }
}
