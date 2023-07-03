package com.cutesmouse.therichman.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TheRichManMoveEvent extends Event {
    private static HandlerList handle = new HandlerList();

    public static HandlerList getHandlerList() {
        return handle;
    }

    private Player player;
    private int from;
    private int dest;
    private int steps;

    public TheRichManMoveEvent(Player player, int from, int dest, int steps) {
        this.player = player;
        this.dest = dest;
        this.from = from;
        this.steps = steps;
    }

    public Player getPlayer() {
        return player;
    }

    public int getFrom() {
        return from;
    }

    public int getDest() {
        return dest;
    }

    public int getSteps() {
        return steps;
    }

    @Override
    public HandlerList getHandlers() {
        return handle;
    }
}
