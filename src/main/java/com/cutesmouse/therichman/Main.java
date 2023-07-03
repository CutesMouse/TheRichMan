package com.cutesmouse.therichman;

import com.cutesmouse.therichman.command.StartCommand;
import com.cutesmouse.therichman.command.TestCommand;
import com.cutesmouse.therichman.gui.GUIListener;
import com.cutesmouse.therichman.listener.DiceListener;
import com.cutesmouse.therichman.listener.GameEventListener;
import com.cutesmouse.therichman.listener.GameItemListener;
import com.cutesmouse.therichman.listener.HouseListener;
import com.cutesmouse.therichman.map.*;
import com.cutesmouse.therichman.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends JavaPlugin {

    private static TheRichMan game;
    public static TheRichMan getGame() {
        return game;
    }

    public static JavaPlugin getPlugin() {
        return Main.getPlugin(Main.class);
    }

    public static final String VERSION = "1.0";

    @Override
    public void onEnable() {
        Bukkit.getWorlds().get(0).getEntities().stream()
                .filter(en -> en.getScoreboardTags().contains("rm_temp")).forEach(Entity::remove);
        var map = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "classic_map.yml"));
        var event = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "settings.yml"));
        game = new TheRichMan();
        game.setPlot(new ConfigPlot(map, "classic_map"));
        game.setEventManager(new ConfigEventManager(event));
        game.showCountryName();
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new HouseListener(), this);
        getServer().getPluginManager().registerEvents(new DiceListener(), this);
        getServer().getPluginManager().registerEvents(new GameEventListener(), this);
        getServer().getPluginManager().registerEvents(new GameItemListener(), this);
        getCommand("start").setExecutor(new StartCommand());
        ScoreboardManager.init();
        ScoreboardManager.setPresetData();
    }

    @Override
    public void onDisable() {
        Bukkit.getWorlds().get(0).getEntities().stream()
                .filter(en -> en.getScoreboardTags().contains("rm_temp")).forEach(Entity::remove);
        Plot plot = Main.getGame().getPlot();
        for (int i = 0; i < plot.getMaxSlot(); i++) {
            Slot s = plot.getSlot(i);
            if (s.hasHouse()) Main.getGame().clearHouse(i);
        }
    }
}
