package eu.aaxvv.node_spell.client.util;

import com.mojang.blaze3d.vertex.BufferBuilder;
import org.joml.Matrix4f;

public class RenderUtil {
    public static void putQuad(Matrix4f mat, BufferBuilder bb, int x, int y, int w, int h, float r, float g, float b) {
        bb.vertex(mat, (float)x, (float)y, 0.0F).color(r, g, b, 1).endVertex();
        bb.vertex(mat, (float)x, (float)y+h, 0.0F).color(r, g, b, 1).endVertex();
        bb.vertex(mat, (float)x+w, (float)y+h, 0.0F).color(r, g, b, 1).endVertex();
        bb.vertex(mat, (float)x+w, (float)y, 0.0F).color(r, g, b, 1).endVertex();
    }

    public static void putQuad(Matrix4f mat, BufferBuilder bb, int x, int y, int w, int h, float r, float g, float b, float a) {
        bb.vertex(mat, (float)x, (float)y, 0.0F).color(r, g, b, a).endVertex();
        bb.vertex(mat, (float)x, (float)y+h, 0.0F).color(r, g, b, a).endVertex();
        bb.vertex(mat, (float)x+w, (float)y+h, 0.0F).color(r, g, b, a).endVertex();
        bb.vertex(mat, (float)x+w, (float)y, 0.0F).color(r, g, b, a).endVertex();
    }
}
