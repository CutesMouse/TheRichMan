package com.cutesmouse.therichman.utils;

import com.cutesmouse.therichman.Main;
import com.cutesmouse.therichman.Profile;
import com.cutesmouse.therichman.event.ThrowDiceEvent;
import com.cutesmouse.therichman.gui.GUI;
import com.cutesmouse.therichman.map.Plot;
import com.cutesmouse.therichman.map.Slot;
import com.cutesmouse.therichman.map.SlotType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameItemGUI {
    public static void openDiceGUI(Player player) {
        GUI gui = new GUI("大富翁 道具選項", 45, player.getName());
        for (int i = 0; i < 45; i++)
            gui.setItem(i, "§7", null, Material.GRAY_STAINED_GLASS_PANE, e -> {
            });
        Profile profile = Main.getGame().getProfile(player.getName());
        Plot plot = Main.getGame().getPlot();
        if (profile == null) return;
        int pos = profile.getPos();

        for (int i = 0; i < 6; i++) {
            int slot_id = (i < 3 ? (11 + 2 * i) : (23 + 2 * i));
            final int point = i + 1;
            String name = "§9前進 " + (i + 1) + " 格";
            ArrayList<String> lore = new ArrayList<>();
            Slot slot = plot.getSlot(plot.moveForward(pos, point));
            lore.add("§6目的地: " + slot.getDisplayName());

            if (slot.getType().isConstructable()) {
                lore.add("");
                if (!slot.hasHouse()) {
                    lore.add("§e目前沒有任何房屋");
                } else {
                    if (slot.getHouseholder().equals(player.getName())) {
                        lore.add("§e有一間您自己的"+slot.getHouseName()+"！");
                    } else {
                        lore.add("§e有一間屬於 " + slot.getHouseholder() + " 的"+slot.getHouseName()+"！");
                    }
                }
            }
            lore.add("");
            lore.add("§e<點擊使用道具>");
            gui.setItem(slot_id, name, lore, Material.MAGENTA_GLAZED_TERRACOTTA, e -> lockDiceResult(player, point), point);
        }
        gui.open();
    }

    public static void openStealGUI(Player player) {
        ArrayList<String> players = Main.getGame().getPlayers();
        int size = (((players.size() - 1) / 9) + 1) * 9;
        GUI gui = new GUI("大富翁 道具選項", size, player.getName());
        for (int i = 0; i < size; i++)
            gui.setItem(i, "§7", null, Material.GRAY_STAINED_GLASS_PANE, e -> {
            });

        int ptr = 0;
        for (String p : players) {
            if (p.equals(player.getName())) continue;
            int money = Main.getGame().getProfile(p).getMoney();
            OfflinePlayer op = Bukkit.getPlayer(p);
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(op);
            meta.setLore(new ArrayList<>(Arrays.asList("§6存款: " + money, "", "§e<點擊使用道具>")));
            head.setItemMeta(meta);
            gui.setItem(ptr, head, e -> stealPlayer(player.getName(), p, 3000));
            ptr++;
        }
        gui.open();
    }

    public static void openMoneyBagGUI(Player player) {
        ArrayList<String> players = Main.getGame().getPlayers();
        GUI gui = new GUI("大富翁 道具選項", 45, player.getName());
        for (int i = 0; i < 45; i++)
            gui.setItem(i, "§7", null, Material.GRAY_STAINED_GLASS_PANE, e -> {
            });

        gui.setItem(22, "§9打開小錢包", Collections.singletonList("§e<點擊使用道具>"), Material.PAPER, e -> useMoneyBag(player));
        gui.open();
    }

    public static void openCleanerGUI(Player player) {
        ArrayList<String> players = Main.getGame().getPlayers();
        int size = (((players.size() - 1) / 9) + 1) * 9;
        GUI gui = new GUI("大富翁 道具選項", size, player.getName());
        for (int i = 0; i < size; i++)
            gui.setItem(i, "§7", null, Material.GRAY_STAINED_GLASS_PANE, e -> {
            });

        int ptr = 0;
        for (String p : players) {
            if (p.equals(player.getName())) continue;
            int money = Main.getGame().getProfile(p).getMoney();
            OfflinePlayer op = Bukkit.getPlayer(p);
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(op);
            meta.setLore(new ArrayList<>(Arrays.asList("§6存款: " + money, "", "§e<點擊使用道具>")));
            head.setItemMeta(meta);
            gui.setItem(ptr, head, e -> clearPlayer(player.getName(), p));
            ptr++;
        }
        gui.open();
    }

    public static void openHotelGUI(Player player) {
        Plot plot = Main.getGame().getPlot();
        ArrayList<Slot> house = new ArrayList<>();
        for (int i = 0; i < plot.getMaxSlot(); i++) {
            Slot slot = plot.getSlot(i);
            if (slot.hasHouse() && slot.getHouseholder().equals(player.getName())) house.add(slot);
        }
        int size = (((house.size() - 1) / 9) + 1) * 9;
        GUI gui = new GUI("大富翁 道具選項", size, player.getName());
        for (int i = 0; i < size; i++)
            gui.setItem(i, "§7", null, Material.GRAY_STAINED_GLASS_PANE, e -> {
            });

        int ptr = 0;
        for (Slot s : house) {
            ArrayList<String> lore = new ArrayList<>();
            Material mat = Material.BEDROCK;
            switch (s.getHouseLevel()) {
                case 1 -> {
                    lore.add("§6一階房屋");
                    mat = Material.OAK_LOG;
                }
                case 2 -> {
                    lore.add("§6二階房屋");
                    mat = Material.COBBLESTONE;
                }
                case 3 -> {
                    lore.add("§6三階房屋");
                    mat = Material.SPRUCE_LOG;
                }
                case 4 -> {
                    lore.add("§6四階房屋");
                    mat = Material.QUARTZ_BLOCK;
                }
            }
            lore.add("");
            lore.add("§e<點擊使用道具>");
            gui.setItem(ptr, s.getDisplayName(),lore, mat, e -> useHotel(player, s));
            ptr++;
        }
        gui.open();
    }

    private static void lockDiceResult(Player p, int point) {
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        if (!mainHand.getType().equals(Material.PAPER) || !GameItem.distinguish(mainHand).equals(GameItem.SPECIAL_DICE)) {
            Main.getGame().privateMessage(p, AnnounceType.TOOL, "§d請手持道具來使用！");
            return;
        }
        Profile profile = Main.getGame().getProfile(p.getName());
        if (!Main.getGame().getTurnPlayer().equals(p.getName()) || profile.isDiceThrown()) {
            Main.getGame().privateMessage(p, AnnounceType.TOOL, "§d現在不可以使用這個道具！");
            return;
        }
        mainHand.setAmount(mainHand.getAmount() - 1);
        p.getInventory().remove(Material.PLAYER_HEAD);
        p.getInventory().setItemInMainHand(mainHand);
        profile.setDicePoint(point);
        Main.getGame().announce(AnnounceType.TOOL, "§a" + p.getName() + " §d使用了不公正的骰子！");
        Main.getPlugin().getServer().getPluginManager().callEvent(new ThrowDiceEvent(p));
    }

    private static void stealPlayer(String stealer, String target, int amount) {
        Player p = Bukkit.getPlayer(stealer);
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        if (!mainHand.getType().equals(Material.PAPER) || !GameItem.distinguish(mainHand).equals(GameItem.STEAL)) {
            Main.getGame().privateMessage(p, AnnounceType.TOOL, "§d請手持道具來使用！");
            return;
        }
        Profile stealer_profile = Main.getGame().getProfile(stealer);
        Profile target_profile = Main.getGame().getProfile(target);
        stealer_profile.addMoney(amount);
        target_profile.addMoney(-amount, true);
        Main.getGame().announce(AnnounceType.TOOL, "§a" + stealer + " §d使用了征討令，從 §a" + target + " §d身上偷走了§a" + amount + "§d元！");
        mainHand.setAmount(mainHand.getAmount() - 1);
        p.getInventory().setItemInMainHand(mainHand);
    }

    private static void useMoneyBag(Player p) {
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        if (!mainHand.getType().equals(Material.PAPER) || !GameItem.distinguish(mainHand).equals(GameItem.MONEYBAG)) {
            Main.getGame().privateMessage(p, AnnounceType.TOOL, "§d請手持道具來使用！");
            return;
        }
        Profile profile = Main.getGame().getProfile(p.getName());
        profile.addMoney(10000);
        Main.getGame().announce(AnnounceType.TOOL, "§a" + p.getName() + " §d使用了小錢包，現在存款 §a" + profile.getMoney() + "§d！");
        mainHand.setAmount(mainHand.getAmount() - 1);
        p.getInventory().setItemInMainHand(mainHand);
    }

    private static void clearPlayer(String stealer, String target) {
        Player p = Bukkit.getPlayer(stealer);
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        if (!mainHand.getType().equals(Material.PAPER) || !GameItem.distinguish(mainHand).equals(GameItem.CLEANER)) {
            Main.getGame().privateMessage(p, AnnounceType.TOOL, "§d請手持道具來使用！");
            return;
        }
        Profile target_profile = Main.getGame().getProfile(target);
        target_profile.resetMoney();
        Main.getGame().announce(AnnounceType.TOOL, "§a" + stealer + " §d使用了盜賊令，清除了 §a" + target + " §d身上所有積蓄！");
        mainHand.setAmount(mainHand.getAmount() - 1);
        p.getInventory().setItemInMainHand(mainHand);
    }

    private static void useHotel(Player p, Slot target) {
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        if (!mainHand.getType().equals(Material.PAPER) || !GameItem.distinguish(mainHand).equals(GameItem.HOTEL)) {
            Main.getGame().privateMessage(p, AnnounceType.TOOL, "§d請手持道具來使用！");
            return;
        }
        Main.getGame().teleport(p, target);
        Main.getGame().announce(AnnounceType.TOOL, "§a" + p.getName() + " §d使用了旅館通行證，移動至 §a" + target.getDisplayName() + " §d！");
        mainHand.setAmount(mainHand.getAmount() - 1);
        p.getInventory().setItemInMainHand(mainHand);
    }
}
