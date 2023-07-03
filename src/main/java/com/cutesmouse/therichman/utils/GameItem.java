package com.cutesmouse.therichman.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum GameItem {
    SPECIAL_DICE(1, "不公正的骰子", getSpecialDice()),
    STEAL(2, "征討令", getSteal()),
    MONEYBAG(3, "小錢包", getMoneyBag()),
    CLEANER(4, "盜賊令", getCleaner()),
    HOTEL(5, "旅館通行證", getHotel()),
    UNKNOWN(-1, null, null);

    private int id;
    private ItemStack item;
    private String name;

    GameItem(int id, String name, ItemStack item) {
        this.id = id;
        this.item = item;
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public static GameItem distinguish(ItemStack item) {
        if (!item.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) return UNKNOWN;
        return switch (item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)) {
            case 1 -> SPECIAL_DICE;
            case 2 -> STEAL;
            case 3 -> MONEYBAG;
            case 4 -> CLEANER;
            case 5 -> HOTEL;
            default -> UNKNOWN;
        };
    }

    public static ItemStack getSpecialDice() {
        return getItem(1, "不公正的骰子", Arrays.asList("§f當普通骰子用", "§f可操控骰子最終點數"));
    }

    public static ItemStack getSteal() {
        return getItem(2, "征討令", Arrays.asList("§f任何時候按右鍵使用", "§f從指定玩家身上盜取3000元", "§f可使金錢為負"));
    }

    public static ItemStack getMoneyBag() {
        return getItem(3, "小錢包", Arrays.asList("§f任何時候按右鍵使用", "§f立刻獲得10000元"));
    }

    public static ItemStack getCleaner() {
        return getItem(4, "盜賊令", Arrays.asList("§f任何時候按右鍵使用", "§f清除指定玩家的所有積蓄"));
    }

    public static ItemStack getHotel() {
        return getItem(5, "旅館通行證", Arrays.asList("§f任何時候按右鍵使用", "§f傳送到自己的房子"));
    }

    private static ItemStack getItem(int id, String name, List<String> lore) {
        ItemStack item = new ItemStack(Material.PAPER);
        item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, id);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName("§6" + name);
        meta.setLore(new ArrayList<>(lore));
        item.setItemMeta(meta);
        return item;
    }
}
