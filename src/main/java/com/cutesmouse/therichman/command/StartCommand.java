package com.cutesmouse.therichman.command;

import com.cutesmouse.therichman.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Main.getGame().start();
        return true;
    }
}
