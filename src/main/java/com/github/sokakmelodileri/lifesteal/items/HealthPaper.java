package com.github.sokakmelodileri.lifesteal.items;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HealthPaper{
    LifeSteal plugin;
    protected HealthPaper(LifeSteal plugin){
        this.plugin = plugin;
    }
    public ItemStack getItem(int amount){
        ItemStack item = new ItemStack(Material.PAPER, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(plugin.getConfig().getString("paper-name").replace("&", "§"));
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = plugin.getConfig().getStringList("paper-lore");
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replace("&", "§"));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}
