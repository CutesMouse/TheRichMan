package com.cutesmouse.therichman.map;


import org.bukkit.Location;

public class Slot {
    private Location location;
    private SlotType type;
    private String name;
    private int angle;
    private int house_level;
    private String householder;
    private final int pos;

    public Slot(Location loc, SlotType type, String name, int angle, int pos) {
        this.location = loc;
        this.type = type;
        this.name = name;
        this.angle = angle;
        this.house_level = 0;
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public int getAngle() {
        return angle;
    }

    public Location getLocation() {
        return location;
    }

    public SlotType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return switch (type) {
            case NORMAL -> "§f" + name;
            case MONEY -> "§a經濟";
            case CHANCE -> "§c機會";
            case FATE -> "§2命運";
            case BONUS -> "§6BONUS";
            case WATER -> "§b自來水公司";
            case BEGIN -> "§5起點";
        };
    }

    public boolean hasHouse() {
        return house_level != 0;
    }

    public String getHouseholder() {
        return householder;
    }

    public int getHouseLevel() {
        return house_level;
    }

    private static final String[] CHINESE_TABLE = {"零", "一", "二", "三", "四"};
    public String getHouseName() {
        return CHINESE_TABLE[house_level]+"階房屋";
    }

    public void setHouse_level(int house_level) {
        this.house_level = house_level;
    }

    public void setHouseholder(String householder) {
        this.householder = householder;
    }
}
