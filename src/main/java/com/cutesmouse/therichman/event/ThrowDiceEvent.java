package com.cutesmouse.therichman.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ThrowDiceEvent extends Event {
    private static final HandlerList handle = new HandlerList();

    public static HandlerList getHandlerList() {
        return handle;
    }
    private Player p;

    public ThrowDiceEvent(Player p) {
        this.p = p;
    }

    public Player getPlayer() {
        return p;
    }

    @Override
    public HandlerList getHandlers() {
        return handle;
    }
}
