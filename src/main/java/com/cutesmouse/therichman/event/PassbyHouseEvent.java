package com.cutesmouse.therichman.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PassbyHouseEvent extends Event {
    private static final HandlerList handle = new HandlerList();

    private Player player;
    private int dest;
    private String houseowner;
    private int houselevel;

    public PassbyHouseEvent(Player player, int dest, String houseowner, int houselevel) {
        this.player = player;
        this.dest = dest;
        this.houselevel = houselevel;
        this.houseowner = houseowner;
    }

    public Player getPlayer() {
        return player;
    }

    public int getDest() {
        return dest;
    }

    public int getHouseLevel() {
        return houselevel;
    }

    public String getHouseOwner() {
        return houseowner;
    }

    public static HandlerList getHandlerList() {
        return handle;
    }

    @Override
    public HandlerList getHandlers() {
        return handle;
    }
}
