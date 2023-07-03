package com.cutesmouse.therichman.listener;

import com.cutesmouse.therichman.Main;
import com.cutesmouse.therichman.TheRichMan;
import com.cutesmouse.therichman.utils.AnnounceType;
import com.cutesmouse.therichman.utils.GameItem;
import com.cutesmouse.therichman.utils.GameItemGUI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameItemListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Main.getGame().getStatus().equals(TheRichMan.GameStatus.ONGOING)) return;
        if (event.getItem() == null) return;
        if (!event.getItem().getType().equals(Material.PAPER)) return;
        GameItem item = GameItem.distinguish(event.getItem());
        if (item.equals(GameItem.UNKNOWN)) return;
        String player = event.getPlayer().getName();
        switch (item) {

            case SPECIAL_DICE -> {
                if (!Main.getGame().getTurnPlayer().equals(player) || Main.getGame().getProfile(player).isDiceThrown()) {
                    Main.getGame().privateMessage(event.getPlayer(), AnnounceType.TOOL, "§d現在不可以使用這個道具！");
                    return;
                }
                GameItemGUI.openDiceGUI(event.getPlayer());
            }
            case STEAL -> {
                GameItemGUI.openStealGUI(event.getPlayer());
            }
            case MONEYBAG -> {
                GameItemGUI.openMoneyBagGUI(event.getPlayer());
            }
            case CLEANER -> {
                GameItemGUI.openCleanerGUI(event.getPlayer());
            }
            case HOTEL -> {
                GameItemGUI.openHotelGUI(event.getPlayer());
            }

        }
    }

}
