package com.cutesmouse.therichman;

import com.cutesmouse.therichman.event.BuyHouseEvent;
import com.cutesmouse.therichman.event.RemoveHouseEvent;
import com.cutesmouse.therichman.event.TheRichManMoveEvent;
import com.cutesmouse.therichman.map.EventManager;
import com.cutesmouse.therichman.map.Plot;
import com.cutesmouse.therichman.map.Slot;
import com.cutesmouse.therichman.map.SlotType;
import com.cutesmouse.therichman.scoreboard.ScoreboardManager;
import com.cutesmouse.therichman.utils.AnnounceType;
import com.cutesmouse.therichman.utils.BuildTools;
import com.cutesmouse.therichman.utils.HouseGUI;
import com.cutesmouse.therichman.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TheRichMan {

    private GameStatus status;
    private ArrayList<String> players;
    private Plot plot;
    private EventManager eventManager;
    private int turn;
    private HashMap<String, Profile> profiles;
    private long startTime;

    public static ItemStack getDiceItemStack() {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = ((SkullMeta) stack.getItemMeta());
        meta.setOwnerProfile(Bukkit.createPlayerProfile("ElliotIsShort"));
        meta.setDisplayName("§f骰子");
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getBuyToolItemStack() {
        ItemStack stack = new ItemStack(Material.BOOK);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("§f房地產廣告");
        meta.setLore(new ArrayList<>(Arrays.asList("§f買房屋？找我就對了", "§fxx不動產")));
        stack.setItemMeta(meta);
        return stack;
    }

    public TheRichMan() {
        profiles = new HashMap<>();
        status = GameStatus.PREPARE;
        players = new ArrayList<>();
    }

    public void setPlot(Plot plot) {
        this.plot = plot;
    }

    public void setEventManager(EventManager manager) {
        this.eventManager = manager;
    }

    public Plot getPlot() {
        return plot;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public void start() {
        resetPlayerStatus();
        startTime = System.currentTimeMillis();
        status = GameStatus.ONGOING;
        players = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(players);
        turn = -1;
        ScoreboardManager.setPresetData();
        switchTurn();
    }

    public String getTimeFormatted() {
        long time = System.currentTimeMillis() - startTime;
        int min = (int) ((time / 1000) / 60);
        int sec = (int) ((time / 1000) % 60);
        return String.format("%02d:%02d", min, sec);
    }

    // Only for scoreboard
    public String getRankMessage(int rank) {
        ArrayList<String> list = new ArrayList<>(players);
        list.sort(Comparator.comparingInt(p -> profiles.get(p).getNetWorth()));
        Collections.reverse(list);
        if (rank > list.size()) return "§e" + rank + ". §6-";
        int networth = profiles.get(list.get(rank - 1)).getNetWorth();
        String networth_msg = "";
        if (networth < 1000) networth_msg = networth + "";
        if (networth >= 1000 && networth < 1000000) networth_msg = (networth / 100) / 10.0 + "K";
        if (networth >= 1000000) networth_msg = (networth / 100000) / 10.0 + "M";
        return "§e" + rank + ". §6" + list.get(rank - 1) + " §7(" + networth_msg + ")";
    }

    public String getTurnPlayer() {
        return players.get(turn);
    }

    public Profile getProfile(String player) {
        return profiles.get(player);
    }

    public void moveForward(Player player, int steps) {
        announce(AnnounceType.EVENT, "§a" + player.getName() + " §d骰出了 §a" + steps + " §d！");
        Profile profile = profiles.get(player.getName());
        int pos = profile.getPos();
        int npos = plot.moveForward(pos, steps);
        teleport(player, plot.getSlot(npos));
        Main.getPlugin().getServer().getPluginManager().callEvent(new TheRichManMoveEvent(player, pos, npos, steps));
    }

    public void teleport(Player player, Slot slot) {
        Location loc = slot.getLocation().clone();
        Slot nextSlot = plot.getSlot(plot.moveForward(slot.getPos(), 1));
        loc.setYaw(MathUtils.getFacingAngle(loc, nextSlot.getLocation()));
        player.teleport(loc);
        profiles.get(player.getName()).setPos(slot.getPos());
    }

    //this kind of move won't trigger events
    public void teleportToNearest(Player player, SlotType type) {
        Profile profile = profiles.get(player.getName());
        int pos = profile.getPos();
        do {
            pos = plot.moveForward(pos, 1);
        } while (!plot.getSlot(pos).getType().equals(type));
        teleport(player, plot.getSlot(pos));
    }

    public void purchaseHouseAt(Player player, int level, int pos) {
        Profile profile = profiles.get(player.getName());
        Slot slot = plot.getSlot(pos);
        int cost = HouseGUI.getHousePrice(level);
        if (!slot.getType().isConstructable()) {
            privateMessage(player, AnnounceType.HOUSE, "§d本位置不可建造任何建築物！");
            return;
        }
        if (cost > profile.getMoney()) {
            privateMessage(player, AnnounceType.HOUSE, "§d您的金錢不足！");
            return;
        }
        Main.getPlugin().getServer().getPluginManager().callEvent(new BuyHouseEvent(player, pos, level, cost));
        player.teleport(slot.getLocation());

        profile.addMoney(-cost);
        profile.addHouseWorth(cost);
        buildHouseAt(level, pos);
        slot.setHouse_level(level);
        slot.setHouseholder(player.getName());
        announce(AnnounceType.HOUSE, "§a" + player.getName() + " §d購買了位於 §a" + slot.getDisplayName() + " §d的" + slot.getHouseName() + "！");
    }

    public void removeHouseAt(Player player, int pos) {
        Slot slot = plot.getSlot(pos);
        Profile profile = profiles.get(player.getName());
        Profile holder = profiles.get(slot.getHouseholder());

        int price = HouseGUI.getHousePrice(slot.getHouseLevel());

        if (!slot.hasHouse()) {
            privateMessage(player, AnnounceType.HOUSE, "§d本位置上沒有任何可移除的建築物！");
            return;
        }
        if (slot.getHouseholder().equals(player.getName())) {
            profile.addMoney(price / 2);
            profile.addHouseWorth(-price);
            announce(AnnounceType.HOUSE, "§a" + player.getName() + " §d拆除了 §a自己" +
                    " §d位於 §a" + slot.getDisplayName() + " §d的" + slot.getHouseName() + "！");
            clearHouse(pos);
            slot.setHouse_level(0);
            slot.setHouseholder(null);
            return;
        }
        if (price > profile.getMoney()) {
            privateMessage(player, AnnounceType.HOUSE, "§d您的金錢不足！");
            return;
        }
        profile.addMoney(-price);
        holder.addHouseWorth(-price);
        holder.addMoney(price);
        Main.getPlugin().getServer().getPluginManager().callEvent(new RemoveHouseEvent(player, Bukkit.getPlayer(slot.getHouseholder()),
                pos, price, slot.getHouseLevel()));

        announce(AnnounceType.HOUSE, "§a" + player.getName() + " §d拆除了 §a" + holder.getPlayer() +
                " §d位於 §a" + slot.getDisplayName() + " §d的" + slot.getHouseName() + "！");
        clearHouse(pos);
        slot.setHouse_level(0);
        slot.setHouseholder(null);
    }

    public void buildHouseAt(int level, int pos) {
        if (plot == null) return;
        File file = plot.getHouseSchematic(level);
        Slot slot = plot.getSlot(pos);
        Location loc = slot.getLocation();
        BuildTools.pasteSchematic(file, loc, slot.getAngle());
    }

    public void clearHouse(int pos) {
        if (plot == null) return;
        File file = plot.getEmptySchematic();
        Slot slot = plot.getSlot(pos);
        Location loc = slot.getLocation();
        BuildTools.pasteSchematic(file, loc, slot.getAngle());
    }

    public void showCountryName() {
        if (plot == null) return;
        for (int i = 0; i < plot.getMaxSlot(); i++) {
            Slot slot = plot.getSlot(i);
            Location loc = slot.getLocation().clone();
            ArmorStand stand = loc.getWorld().spawn(loc.add(0, 2.5, 0), ArmorStand.class);
            stand.setCustomName(slot.getDisplayName());
            stand.setCustomNameVisible(true);
            stand.setGravity(false);
            stand.setInvisible(true);
            stand.setMarker(true);
            stand.setInvulnerable(true);
            stand.getScoreboardTags().add("rm_temp");
        }
    }

    public GameStatus getStatus() {
        return status;
    }

    //綠色(a)人名 粉色(d)字
    public void announce(AnnounceType type, String s) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(type + " §a§l大§6§l富§b§l翁" + type.getColor() + "§l> " + s);
        }
    }

    public void privateMessage(Player p, AnnounceType type, String s) {
        p.sendMessage(type + " §a§l大§6§l富§b§l翁§9§l> " + s);
    }

    public void switchTurn() {
        turn++;
        turn = turn % (players.size());
        Player host = Bukkit.getPlayer(players.get(turn));
        Profile profile = profiles.get(players.get(turn));
        if (profile.isStopped()) {
            profile.setStopped(false);
            switchTurn();
            return;
        }
        if (profile.isNausea()) {
            profile.setNausea(false);
        }
        profile.addMoney(200);
        host.getInventory().addItem(getDiceItemStack());
        announce(AnnounceType.EVENT, "§d現在輪到 §a" + host.getName() + " §d！");
    }

    private void resetPlayerStatus() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            p.setFoodLevel(20);
            p.getInventory().clear();
            p.teleport(getPlot().getBegin().getLocation());
            p.setGameMode(GameMode.ADVENTURE);
            Profile profile = new Profile(p.getName());
            profile.setMoney(2000);
            profiles.put(p.getName(), profile);
            p.getInventory().setItem(8, getBuyToolItemStack());
        }
    }

    public enum GameStatus {
        ONGOING, END, PREPARE
    }
}


