package com.cutesmouse.therichman.command;

import com.cutesmouse.therichman.Main;
import com.cutesmouse.therichman.TheRichMan;
import com.cutesmouse.therichman.gui.GUI;
import com.cutesmouse.therichman.map.Plot;
import com.cutesmouse.therichman.map.Slot;
import com.cutesmouse.therichman.utils.GameItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Main.getGame().start();
        return true;
    }
}
