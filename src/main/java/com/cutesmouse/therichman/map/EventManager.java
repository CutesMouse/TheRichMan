package com.cutesmouse.therichman.map;

import java.util.ArrayList;

public interface EventManager {
    ArrayList<GameEvent> getFateEvents();
    ArrayList<GameEvent> getChanceEvents();
    ArrayList<GameEvent> getMoneyEvents();
    ArrayList<GameEvent> getBonusEvents();
}
