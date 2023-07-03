package com.cutesmouse.therichman.map;

import com.cutesmouse.therichman.utils.EventType;

public class GameEvent {
    private String description;
    private String value;
    private EventType type;
    public GameEvent(String description, String value, EventType type) {
        this.description = description;
        this.value = value;
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }
}
