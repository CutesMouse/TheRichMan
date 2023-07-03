package com.cutesmouse.therichman.map;

import com.cutesmouse.therichman.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ConfigPlot implements Plot {
    private final ArrayList<Slot> slots;
    private final String confName;
    public ConfigPlot(YamlConfiguration config, String confName) {
        slots = new ArrayList<>();
        this.confName = confName;
        int ptr = 0;

        for (String key : config.getKeys(false)) {
            String name = key;
            String type_string = config.getString(key+".type");
            Integer[] location_string = Arrays.stream(config.getString(key + ".location").split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
            int angle = config.getInt(key+".angle");
            SlotType type = SlotType.valueOf(type_string);
            Location loc = new Location(Bukkit.getWorlds().get(0), location_string[0]+0.5, location_string[1], location_string[2]+0.5, angle, 0);
            Slot slot = new Slot(loc, type, name, angle, ptr);
            ptr++;
            slots.add(slot);
        }

    }

    @Override
    public File getHouseSchematic(int level) {
        return new File(Main.getPlugin().getDataFolder(), confName+"/house_"+level+".schem");
    }

    @Override
    public File getEmptySchematic() {
        return new File(Main.getPlugin().getDataFolder(), confName+"/empty.schem");
    }

    @Override
    public int moveForward(int pos, int steps) {
        return (pos+steps) % getMaxSlot();
    }

    @Override
    public Slot getBegin() {
        return getSlot(0);
    }

    @Override
    public Slot getSlot(int i) {
        return slots.get(i);
    }

    @Override
    public int getMaxSlot() {
        return slots.size();
    }

    public String getConfigName() {
        return confName;
    }
}
