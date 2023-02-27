package eu.aaxvv.node_spell.util;

import net.minecraft.world.phys.Vec3;

public class VectorUtil {
    public static Vec3 nearestAxis(Vec3 vec) {
        double absX = Math.abs(vec.x);
        double absY = Math.abs(vec.y);
        double absZ = Math.abs(vec.z);

        if (absX >= absY && absX >= absZ) {
            return vec.x >= 0 ? new Vec3(1, 0, 0) : new Vec3(-1, 0, 0);
        } else if (absZ >= absX && absZ >= absY) {
            return vec.z >= 0 ? new Vec3(0, 0, 1) : new Vec3(0, 0, -1);
        } else {
            return vec.y >= 0 ? new Vec3(0, 1, 0) : new Vec3(0, -1, 0);
        }
    }
}
