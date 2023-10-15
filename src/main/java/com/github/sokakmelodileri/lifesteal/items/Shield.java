package com.github.sokakmelodileri.lifesteal.items;

import com.github.sokakmelodileri.lifesteal.LifeSteal;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Shield {
    LifeSteal plugin;

    public Shield(LifeSteal plugin) {
        this.plugin = plugin;
    }

    public ItemStack getShield(int amount) {
        ItemStack item = new ItemStack(Material.NETHER_BRICK, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(plugin.getConfig().getString("shield-name").replace("&", "§"));
        List<String> lores = plugin.getConfig().getStringList("shield-lore");
        for (int i = 0; lores.size() > i; i++) {
            lores.set(i, lores.get(i).replace("&", "§"));
        }
        meta.setLore(lores);
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

}
