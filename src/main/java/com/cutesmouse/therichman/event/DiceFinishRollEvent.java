package com.cutesmouse.therichman.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DiceFinishRollEvent extends Event {
    private static final HandlerList handle = new HandlerList();

    public static HandlerList getHandlerList() {
        return handle;
    }
    private final Player player;
    private int points;

    public DiceFinishRollEvent(Player player, int points) {
        this.player = player;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public HandlerList getHandlers() {
        return handle;
    }
}
