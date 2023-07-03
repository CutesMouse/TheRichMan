package com.cutesmouse.therichman.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RemoveHouseEvent extends Event {
    private static final HandlerList handle = new HandlerList();

    public static HandlerList getHandlerList() {
        return handle;
    }

    private Player remover;
    private Player householder;
    private int slot;
    private int cost;
    private int level;

    public RemoveHouseEvent(Player remover, Player householder, int slot, int cost, int level) {
        this.remover = remover;
        this.householder = householder;
        this.slot = slot;
        this.cost = cost;
        this.level = level;
    }

    public Player getRemover() {
        return remover;
    }

    public Player getHouseholder() {
        return householder;
    }

    public int getSlot() {
        return slot;
    }

    public int getCost() {
        return cost;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public HandlerList getHandlers() {
        return handle;
    }
}
