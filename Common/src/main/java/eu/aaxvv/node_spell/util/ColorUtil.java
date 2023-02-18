package eu.aaxvv.node_spell.util;

public class ColorUtil {
    public static int packColor(float r, float g, float b, float a) {
        int res = (int)(a * 255) << 24;
        res |= (int)(r * 255) << 16;
        res |= (int)(g * 255) << 8;
        res |= (int)(b * 255);

        return res;
    }

    public static void unpackColor(int packedColor, float[] out) {
        out[0] = (float)(packedColor >> 24 & 255) / 255.0F;
        out[1] = (float)(packedColor >> 16 & 255) / 255.0F;
        out[2] = (float)(packedColor >> 8 & 255) / 255.0F;
        out[3] = (float)(packedColor & 255) / 255.0F;
    }
}
