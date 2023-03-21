package eu.aaxvv.node_spell.util;

import net.minecraft.world.phys.Vec3;
import org.joml.Math;

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

    public static Vec3 rotateAxis(Vec3 vector, double angle, Vec3 axis) {
        if (axis.y == 0.0f && axis.z == 0.0f && absEqualsOne(axis.x))
            return vector.xRot((float)(axis.x * angle));
        else if (axis.x == 0.0f && axis.z == 0.0f && absEqualsOne(axis.y))
            return vector.yRot((float)(axis.y * angle));
        else if (axis.x == 0.0f && axis.y == 0.0f && absEqualsOne(axis.z))
            return vector.zRot((float)(axis.z * angle));
        return rotateAxisInternal(vector, angle, axis);
    }
    private static Vec3 rotateAxisInternal(Vec3 vector, double angle, Vec3 axis) {
        double hAngle = angle * 0.5f;
        double sinAngle = Math.sin(hAngle);
        double qx = axis.x * sinAngle, qy = axis.y * sinAngle, qz = axis.z * sinAngle;
        double qw = Math.cosFromSin(sinAngle, hAngle);
        double w2 = qw * qw, x2 = qx * qx, y2 = qy * qy, z2 = qz * qz, zw = qz * qw;
        double xy = qx * qy, xz = qx * qz, yw = qy * qw, yz = qy * qz, xw = qx * qw;
        double x = vector.x, y = vector.y, z = vector.z;
        double resX = (w2 + x2 - z2 - y2) * x + (-zw + xy - zw + xy) * y + (yw + xz + xz + yw) * z;
        double resY = (xy + zw + zw + xy) * x + ( y2 - z2 + w2 - x2) * y + (yz + yz - xw - xw) * z;
        double resZ = (xz - yw + xz - yw) * x + ( yz + yz + xw + xw) * y + (z2 - y2 - x2 + w2) * z;
        return new Vec3(resX, resY, resZ);
    }

    private static boolean absEqualsOne(double r) {
        return (Double.doubleToRawLongBits(r) & 0x7FFFFFFFFFFFFFFFL) == 0x3FF0000000000000L;
    }
}
