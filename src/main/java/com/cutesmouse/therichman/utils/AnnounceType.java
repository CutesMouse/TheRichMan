package com.cutesmouse.therichman.utils;

public enum AnnounceType {
    EVENT("§9§l[事件]","§9"), HOUSE("§f§l[房仲]","§f"),
    FATE("§2§l[命運]","§2"), CHANCE("§c§l[機會]","§c"),
    TOOL("§6§l[道具]","§6"), WATER("§b§l[自來水公司]","§b"),
    BONUS("§e§l[Bonus]","§e"), MONEY("§a§l[經濟]","§a");
    private final String represent;
    private final String color;
    AnnounceType(String s, String color) {
        this.represent = s;
        this.color = color;
    }

    @Override
    public String toString() {
        return represent;
    }

    public String getColor() {
        return color;
    }
}
