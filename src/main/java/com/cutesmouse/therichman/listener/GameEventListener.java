package com.cutesmouse.therichman.listener;

import com.cutesmouse.therichman.Main;
import com.cutesmouse.therichman.Profile;
import com.cutesmouse.therichman.event.PassbyHouseEvent;
import com.cutesmouse.therichman.event.TheRichManMoveEvent;
import com.cutesmouse.therichman.map.GameEvent;
import com.cutesmouse.therichman.map.Slot;
import com.cutesmouse.therichman.map.SlotType;
import com.cutesmouse.therichman.utils.AnnounceType;
import com.cutesmouse.therichman.utils.GameItem;
import com.cutesmouse.therichman.utils.HouseGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Random;

public class GameEventListener implements Listener {
    private static final Random random = new Random();

    @EventHandler
    public void eventCaller(TheRichManMoveEvent event) {
        Slot dest = Main.getGame().getPlot().getSlot(event.getDest());
        String player = event.getPlayer().getName();
        if (dest.getType().equals(SlotType.NORMAL)) return;
        if (dest.getType().equals(SlotType.WATER)) {
            Main.getGame().announce(AnnounceType.WATER, "§a" + player + " §d經過自來水公司，繳交水電費2000元");
            Main.getGame().getProfile(player).addMoney(-2000);
            return;
        }
        if (dest.getType().equals(SlotType.BEGIN)) {
            GameItem item = GameItem.values()[random.nextInt(GameItem.values().length-1)];
            Main.getGame().announce(AnnounceType.TOOL, "§a" + player + " §d返回起點！獲得道具 §a" + item.getName() + " §d！");
            event.getPlayer().getInventory().addItem(item.getItem());
            return;
        }
        ArrayList<GameEvent> events;
        AnnounceType type;
        switch (dest.getType()) {
            case FATE:
                events = Main.getGame().getEventManager().getFateEvents();
                type = AnnounceType.FATE;
                break;
            case BONUS:
                events = Main.getGame().getEventManager().getBonusEvents();
                type = AnnounceType.BONUS;
                break;
            case MONEY:
                events = Main.getGame().getEventManager().getMoneyEvents();
                type = AnnounceType.MONEY;
                break;
            case CHANCE:
                events = Main.getGame().getEventManager().getChanceEvents();
                type = AnnounceType.CHANCE;
                break;
            default:
                events = null;
                type = null;
        }
        assert events != null;
        GameEvent revent = events.get(random.nextInt(events.size()));
        Main.getGame().announce(type, "§d" + revent.getDescription());
        Profile profile = Main.getGame().getProfile(player);
        switch (revent.getType()) {

            case MONEY -> {
                int change = Integer.parseInt(revent.getValue());
                profile.addMoney(change);
            }
            case STOP -> {
                profile.setStopped(true);
            }
            case RESET -> {
                profile.resetMoney();
            }
            case TELEPORT -> {
                Main.getGame().teleportToNearest(event.getPlayer(), SlotType.valueOf(revent.getValue()));
            }
            case NOTHING -> {

            }
            case BLIND -> {
                profile.setBlind(true);
            }
            case NAUSEA -> {
                profile.setNausea(true);
            }
        }
    }

    @EventHandler
    public void returnToBegin(TheRichManMoveEvent event) {
        if (event.getFrom() + event.getSteps() < Main.getGame().getPlot().getMaxSlot()) return;
        Profile profile = Main.getGame().getProfile(event.getPlayer().getName());
        if (profile.isBlind()) {
            profile.setBlind(false);
            return;
        }
        Main.getGame().announce(AnnounceType.EVENT, "§a" + event.getPlayer().getName() + " §d完成了一圈！獲得2000元禮金！");
        profile.addMoney(2000);
        profile.addRound();
    }



    @EventHandler
    public void passBy(PassbyHouseEvent event) {
        if (event.getHouseOwner().equals(event.getPlayer().getName())) return;
        Profile houseowner = Main.getGame().getProfile(event.getHouseOwner());
        Profile player = Main.getGame().getProfile(event.getPlayer().getName());
        int pass_fee = HouseGUI.getPassFee(event.getHouseLevel());
        Slot slot = Main.getGame().getPlot().getSlot(event.getDest());

        houseowner.addMoney(pass_fee);
        player.addMoney(-pass_fee);
        Main.getGame().announce(AnnounceType.HOUSE,
                "§a" + event.getPlayer().getName() + " §d經過了 §a"
                        + event.getHouseOwner() + " §d的 §a" + slot.getHouseName() +
                        " §d支付過路費 §a" + pass_fee + " §d元！");
    }
}
