package com.cutesmouse.therichman.listener;

import com.cutesmouse.therichman.Main;
import com.cutesmouse.therichman.Profile;
import com.cutesmouse.therichman.TheRichMan;
import com.cutesmouse.therichman.event.DiceFinishRollEvent;
import com.cutesmouse.therichman.event.ThrowDiceEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class DiceListener implements Listener {
    @EventHandler
    public void playerThrowItem(PlayerDropItemEvent e) {
        if (!e.getItemDrop().getItemStack().getType().equals(Material.PLAYER_HEAD)) return;
        if (!Main.getGame().getStatus().equals(TheRichMan.GameStatus.ONGOING)) return;
        e.getItemDrop().remove();
        Main.getGame().getProfile(e.getPlayer().getName()).setDiceThrown(true);
        Main.getPlugin().getServer().getPluginManager().callEvent(new ThrowDiceEvent(e.getPlayer()));
    }

    @EventHandler
    public void diceRolling(ThrowDiceEvent e) {
        new BukkitRunnable() {
            private int tick = 0;
            private final Random random = new Random();

            @Override
            public void run() {
                String title = "§6骰子滾動中...";
                StringBuilder subtitle = new StringBuilder();
                String source = "①②③④⑤⑥";
                Profile profile = Main.getGame().getProfile(e.getPlayer().getName());
                tick++;
                int r = random.nextInt(6);
                if (tick == 10 && profile.getDicePoint() != -1) r = profile.getDicePoint()-1;
                for (int i = 0; i < 6; i++) {
                    if (i == r) subtitle.append("§a");
                    else subtitle.append("§f");
                    subtitle.append(source.charAt(i));
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    player.sendTitle(title, subtitle.toString(), 0, 20, 0);
                }
                if (tick == 10) {
                    int points = r + 1;
                    profile.setDicePoint(-1);
                    DiceFinishRollEvent de = new DiceFinishRollEvent(e.getPlayer(), points);
                    Main.getPlugin().getServer().getPluginManager().callEvent(de);
                    points = de.getPoints();
                    Main.getGame().moveForward(e.getPlayer(), points);
                    Main.getGame().getProfile(e.getPlayer().getName()).setDiceThrown(false);
                    Main.getGame().switchTurn();
                    this.cancel();
                    return;
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 5L);
    }
}
