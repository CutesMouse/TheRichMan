package com.cutesmouse.therichman.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GUI {

    private Inventory inv;
    private final String player;
    private final String name;
    private HashMap<Integer, ClickEvent> eventTable;

    public GUI(String name, int size, String player) {
        inv = Bukkit.createInventory(null, size, name);
        eventTable = new HashMap<>();
        this.player = player;
        this.name = name;
        GUIListener.registerGUI(this);
    }

    public String getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public Inventory getInv() {
        return inv;
    }

    public void setItem(int pos, String name, List<String> lore, Material type, ClickEvent e) {
        setItem(pos, name, lore, type, e, 1);
    }
    public void setItem(int pos, String name, List<String> lore, Material type, ClickEvent e, int amount) {
        ItemStack stack = new ItemStack(type);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        if (lore != null) lore = new ArrayList<>(lore);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        stack.setAmount(amount);
        setItem(pos, stack, e);
    }

    public void setItem(int pos, ItemStack stack, ClickEvent e) {
        inv.setItem(pos, stack);
        eventTable.put(pos, e);
    }

    public void invoke(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!eventTable.containsKey(event.getSlot())) return;
        eventTable.get(event.getSlot()).onClick(event);
        event.getWhoClicked().closeInventory();
    }


    public void open() {
        Bukkit.getPlayer(player).openInventory(inv);
    }
}
