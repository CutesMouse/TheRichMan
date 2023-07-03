package com.cutesmouse.therichman.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;

public class GUIListener implements Listener {
    private static ArrayList<GUI> guis = new ArrayList<>();
    public static void registerGUI(GUI gui) {
        guis.add(gui);
    }
    public static void unregister(GUI gui) {
        guis.remove(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) return;
        for (int i = 0; i < guis.size(); i++) {
            GUI gui = guis.get(i);
            if (e.getWhoClicked().getName().equals(gui.getPlayer())
                    && e.getView().getTitle().equals(gui.getName())) gui.invoke(e);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        guis.removeIf(gui -> gui.getPlayer().equals(e.getPlayer().getName()));
    }
}
