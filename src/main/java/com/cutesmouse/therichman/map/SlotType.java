package com.cutesmouse.therichman.map;

public enum SlotType {
    NORMAL(true), MONEY, CHANCE, FATE, BONUS, WATER(true), BEGIN;

    SlotType(boolean cons) {
        this.constructable = cons;
    }
    SlotType() {
        this.constructable = false;
    }
    private boolean constructable;

    public boolean isConstructable() {
        return constructable;
    }
}
