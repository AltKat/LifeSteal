package com.github.sokakmelodileri.lifesteal.items;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ReviveStick {
    LifeSteal plugin;
    protected ReviveStick(LifeSteal plugin){
        this.plugin = plugin;
    }

    public ItemStack getStick(int amount){
        ItemStack item = new ItemStack(Material.STICK, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(plugin.getConfig().getString("stick-name").replace("&", "§"));
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lores = plugin.getConfig().getStringList("stick-lore");
        for(int i = 0; i < lores.size(); i++){
            lores.set(i, lores.get(i).replace("&", "§"));
        }
        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;
    }
}
