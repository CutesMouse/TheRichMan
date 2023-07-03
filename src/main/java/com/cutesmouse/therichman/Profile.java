package com.cutesmouse.therichman;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Profile {
    private final String player;
    private int pos;
    private int money;
    private boolean stopped;
    private boolean blind;
    private boolean nausea;
    private int houseworth;
    private int dice_point;
    private boolean dice_thrown;
    private int round;

    public Profile(String player) {
        this.player = player;
        pos = 0;
        money = 0;
        houseworth = 0;
        round = 0;
        stopped = false;
        blind = false;
        nausea = false;
        dice_thrown = false;
        dice_point = -1;
    }

    public void addRound() {
        round++;
    }

    public int getRound() {
        return round;
    }

    public boolean isStopped() {
        return stopped;
    }

    public boolean isBlind() {
        return blind;
    }

    public boolean isDiceThrown() {
        return dice_thrown;
    }

    public boolean isNausea() {
        return nausea;
    }

    public int getPos() {
        return pos;
    }

    public int getMoney() {
        return money;
    }

    public int getDicePoint() {
        return dice_point;
    }

    public int getHouseworth() {
        return houseworth;
    }

    public int getNetWorth() {
        return money + houseworth;
    }

    public String getPlayer() {
        return player;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setDiceThrown(boolean dice_thrown) {
        this.dice_thrown = dice_thrown;
    }

    public void setHouseWorth(int houseworth) {
        this.houseworth = houseworth;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public void setDicePoint(int dice_point) {
        this.dice_point = dice_point;
    }

    public void setNausea(boolean nausea) {
        this.nausea = nausea;
        Player player = Bukkit.getPlayer(this.player);
        if (nausea) {
            PotionEffect effect = new PotionEffect(PotionEffectType.CONFUSION, PotionEffect.INFINITE_DURATION, 0, true, true);
            player.addPotionEffect(effect);
        } else {
            player.removePotionEffect(PotionEffectType.CONFUSION);
        }
    }

    public void setBlind(boolean blind) {
        this.blind = blind;
        Player player = Bukkit.getPlayer(this.player);
        if (blind) {
            PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, PotionEffect.INFINITE_DURATION, 0, true, true);
            player.addPotionEffect(effect);
        } else {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }

    public void addMoney(int amount) {
        addMoney(amount, false);
    }

    public void resetMoney() {
        money = 0;
    }

    public void addMoney(int amount, boolean force) {
        if (this.money < 0) force = true;
        this.money += amount;
        if (!force) this.money = Math.max(this.money, 0);
    }


    public void addHouseWorth(int amount) {
        this.houseworth += amount;
    }
}
