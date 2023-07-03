package com.cutesmouse.therichman.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BuyHouseEvent extends Event {
    private static final HandlerList handle = new HandlerList();

    private Player buyer;
    private int slot;
    private int level;
    private int price;

    public BuyHouseEvent(Player player, int slot, int level, int price) {
        this.buyer = player;
        this.slot = slot;
        this.level = level;
        this.price = price;
    }

    public Player getBuyer() {
        return buyer;
    }

    public int getLevel() {
        return level;
    }

    public int getPrice() {
        return price;
    }

    public int getSlot() {
        return slot;
    }

    public static HandlerList getHandlerList() {
        return handle;
    }

    @Override
    public HandlerList getHandlers() {
        return handle;
    }
}
