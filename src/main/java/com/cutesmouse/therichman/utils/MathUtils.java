package com.cutesmouse.therichman.utils;

import org.bukkit.Location;

public class MathUtils {
    public static float getFacingAngle(Location from, Location to) {
        return (float) Math.toDegrees(Math.atan2(from.getX() - to.getX(), to.getZ() - from.getZ()));
    }
}
