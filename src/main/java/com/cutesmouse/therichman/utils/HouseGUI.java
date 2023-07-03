package com.cutesmouse.therichman.utils;

import com.cutesmouse.therichman.Main;
import com.cutesmouse.therichman.gui.GUI;
import com.cutesmouse.therichman.map.Slot;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Arrays;

public class HouseGUI {
    public static void openHouseGUI(Player player, int slot) {
        GUI gui = new GUI("大富翁 房屋購買選單", 45, player.getName());
        Slot s = Main.getGame().getPlot().getSlot(slot);
        for (int i = 0; i < 45; i++) {
            gui.setItem(i, "§9", null, Material.GRAY_STAINED_GLASS_PANE, e -> {
            });
        }
        if (s.hasHouse()) {
            String holder = s.getHouseholder();
            int price = getHousePrice(s.getHouseLevel());
            boolean self_remove = (holder.equals(player.getName()));
            int cost = self_remove ? (-price / 2) : price;
            String warming = self_remove ? "§c拆除後將損失50%購買金額" : "§c此金額將會全數退還給房屋持有者";
            DecimalFormat format = new DecimalFormat("#,###");
            gui.setItem(22, "§c拆除房屋", Arrays.asList("§f此選項曾讓無數有情破裂", "§f拆除後無法復原", "", "§6工程價: $" + format.format(cost), warming, "§e<點擊開始拆除>"),
                    Material.BARRIER, e -> Main.getGame().removeHouseAt(player, slot));
            gui.open();
            return;
        }
        gui.setItem(19, "§9一階房屋",
                Arrays.asList("§f最基礎、簡約的房屋", "§f適合社畜", "", "§6建價: $2,000", "§6過路費: $400", "§e<點擊開始建造>"),
                Material.OAK_LOG, e -> Main.getGame().purchaseHouseAt(player, 1, slot));
        gui.setItem(21, "§9二階房屋",
                Arrays.asList("§f石板屋，看起來非常沉重", "§f原住民的啦！", "", "§6建價: $4,000", "§6過路費: $800", "§e<點擊開始建造>"),
                Material.COBBLESTONE, e -> Main.getGame().purchaseHouseAt(player, 2, slot));
        gui.setItem(23, "§9三階房屋",
                Arrays.asList("§f高山摘採的檜木，數量非常稀少", "§f真香", "", "§6建價: $6,000", "§6過路費: $1,200", "§e<點擊開始建造>"),
                Material.SPRUCE_LOG, e -> Main.getGame().purchaseHouseAt(player, 3, slot));
        gui.setItem(25, "§9四階房屋",
                Arrays.asList("§f豪宅，應有盡有，非常高級", "§f結婚必備", "", "§6建價: $10,000", "§6過路費: $2,000", "§e<點擊開始建造>"),
                Material.QUARTZ_BLOCK, e -> Main.getGame().purchaseHouseAt(player, 4, slot));
        gui.open();
    }

    public static int getHousePrice(int level) {
        return switch (level) {
            case 1 -> 2000;
            case 2 -> 4000;
            case 3 -> 6000;
            case 4 -> 10000;
            default -> 0;
        };
    }

    public static int getPassFee(int level) {
        return (int) (getHousePrice(level) * 0.2);
    }
}
