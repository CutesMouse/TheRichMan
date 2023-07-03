package com.cutesmouse.therichman.listener;

import com.cutesmouse.therichman.utils.AnnounceType;
import com.cutesmouse.therichman.Main;
import com.cutesmouse.therichman.Profile;
import com.cutesmouse.therichman.TheRichMan;
import com.cutesmouse.therichman.event.PassbyHouseEvent;
import com.cutesmouse.therichman.event.TheRichManMoveEvent;
import com.cutesmouse.therichman.map.Slot;
import com.cutesmouse.therichman.utils.HouseGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class HouseListener implements Listener {
    @EventHandler
    public void onMoveHouseTool(InventoryClickEvent e) {
        if (!Main.getGame().getStatus().equals(TheRichMan.GameStatus.ONGOING)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (!Main.getGame().getStatus().equals(TheRichMan.GameStatus.ONGOING)) return;
        if (!e.getItemDrop().getItemStack().getType().equals(Material.BOOK)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!Main.getGame().getStatus().equals(TheRichMan.GameStatus.ONGOING)) return;
        if (e.getItem() == null) return;
        if (!e.getItem().getType().equals(Material.BOOK)) return;
        e.setCancelled(true);
        Profile profile = Main.getGame().getProfile(e.getPlayer().getName());
        int pos = profile.getPos();
        Slot slot = Main.getGame().getPlot().getSlot(pos);
        if (!slot.getType().isConstructable()) {
            Main.getGame().privateMessage(e.getPlayer(), AnnounceType.HOUSE, "§d本位置不可建造任何建築物！");
            return;
        }
        HouseGUI.openHouseGUI(e.getPlayer(), pos);
    }

    @EventHandler
    public void onPlayerMove(TheRichManMoveEvent e) {
        Player player = e.getPlayer();
        Slot slot = Main.getGame().getPlot().getSlot(e.getDest());
        if (slot.hasHouse()) {
            Main.getPlugin().getServer().getPluginManager()
                    .callEvent(new PassbyHouseEvent(player, e.getDest(), slot.getHouseholder(), slot.getHouseLevel()));
        }
    }

}
