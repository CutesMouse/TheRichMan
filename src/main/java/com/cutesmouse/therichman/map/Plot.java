package com.cutesmouse.therichman.map;

import java.io.File;

public interface Plot {
    Slot getBegin();
    Slot getSlot(int i);
    int getMaxSlot();
    File getHouseSchematic(int level);
    File getEmptySchematic();
    int moveForward(int pos, int steps);
}
