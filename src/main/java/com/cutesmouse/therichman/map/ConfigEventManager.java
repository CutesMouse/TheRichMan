package com.cutesmouse.therichman.map;

import com.cutesmouse.therichman.utils.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class ConfigEventManager implements EventManager{
    private final ArrayList<GameEvent> fate;
    private final ArrayList<GameEvent> chance;
    private final ArrayList<GameEvent> money;
    private final ArrayList<GameEvent> bonus;
    public ConfigEventManager(YamlConfiguration config) {
        fate = analyze(config.getConfigurationSection("FATE"));
        chance = analyze(config.getConfigurationSection("CHANCE"));
        money = analyze(config.getConfigurationSection("MONEY"));
        bonus = analyze(config.getConfigurationSection("BONUS"));
    }
    private ArrayList<GameEvent> analyze(ConfigurationSection section) {
        ArrayList<GameEvent> events = new ArrayList<>();
        if (section == null) return events;
        for (String key : section.getKeys(false)) {
            String description = section.getString(key+".event");
            String value = section.getString(key+".value");
            EventType type = EventType.valueOf(section.getString(key+".action"));
            events.add(new GameEvent(description, value, type));
        }
        return events;
    }
    @Override
    public ArrayList<GameEvent> getFateEvents() {
        return fate;
    }

    @Override
    public ArrayList<GameEvent> getChanceEvents() {
        return chance;
    }

    @Override
    public ArrayList<GameEvent> getMoneyEvents() {
        return money;
    }

    @Override
    public ArrayList<GameEvent> getBonusEvents() {
        return bonus;
    }
}
