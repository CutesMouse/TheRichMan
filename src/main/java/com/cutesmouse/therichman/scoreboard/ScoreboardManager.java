package com.cutesmouse.therichman.scoreboard;

import com.cutesmouse.therichman.Main;
import com.cutesmouse.therichman.Profile;
import com.cutesmouse.therichman.TheRichMan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

public class ScoreboardManager {
    private static HashMap<String, HashMap<Integer, String>> last_status;
    private static HashMap<Integer, Function<Player, String>> generators;

    public static void init() {
        last_status = new HashMap<>();
        generators = new HashMap<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                reloadScoreboard();
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);
    }

    public static void setPresetData() {
        if (Main.getGame().getStatus().equals(TheRichMan.GameStatus.ONGOING)) {
            setData(8, p -> "§9錢包: §6" + Main.getGame().getProfile(p.getName()).getMoney());
            setData(7, p -> "§9身價: §6" + Main.getGame().getProfile(p.getName()).getNetWorth());
            setData(6, p -> "§9遊戲時間: §6" + Main.getGame().getTimeFormatted());
            setData(5, p -> "§d輪到 §a" + Main.getGame().getTurnPlayer()+" §d擲骰子");
            setData(4, p -> "§e----富豪榜----");
            setData(3, p -> Main.getGame().getRankMessage(1));
            setData(2, p -> Main.getGame().getRankMessage(2));
            setData(1, p -> Main.getGame().getRankMessage(3));
            setData(0, p -> "TheRichMan v" + Main.VERSION);
        } else {
            setData(8, p -> "§a");
            setData(7, p -> "§d歡迎來到 §a§l大§6§l富§b§l翁");
            setData(6, p -> "§b");
            setData(5, p -> "§7Dev by");
            setData(4, p -> "§7  CutesMouse");
            setData(3, p -> "§c");
            setData(2, p -> "§d現在時間: §a" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            setData(1, p -> "§d");
            setData(0, p -> "TheRichMan v" + Main.VERSION);
        }
    }

    public static void setData(int score, Function<Player, String> title) {
        generators.put(score, title);
    }

    private static void reloadScoreboard() {
        for (Player p : Bukkit.getOnlinePlayers()) {

            if (p.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null || last_status.get(p.getName()) == null) {
                initPlayer(p);
            }

            Scoreboard sc = p.getScoreboard();
            Objective obj = sc.getObjective(DisplaySlot.SIDEBAR);
            Objective list = sc.getObjective(DisplaySlot.PLAYER_LIST);

            generators.forEach((score, title) -> {
                String last = last_status.get(p.getName()).get(score);
                String update = title.apply(p);
                if (last != null && !last.equals(update)) sc.resetScores(last);
                obj.getScore(update).setScore(score);
                last_status.get(p.getName()).put(score, update);
            });

            for (Player player : Bukkit.getOnlinePlayers()) {
                Profile profile = Main.getGame().getProfile(player.getName());
                int round = 0;
                if (profile != null) round = profile.getRound();
                list.getScore(player.getName()).setScore(round);
            }
        }
    }

    public static void initPlayer(Player p) {
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        Scoreboard sc = p.getScoreboard();
        Objective obj = sc.registerNewObjective("mouse", Criteria.DUMMY, "§9◤§a§l大§6§l富§b§l翁§9◢");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Objective list = sc.registerNewObjective("round", Criteria.DUMMY, "round");
        list.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        last_status.put(p.getName(), new HashMap<>());
    }
}
