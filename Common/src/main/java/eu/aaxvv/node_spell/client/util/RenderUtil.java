package eu.aaxvv.node_spell.client.util;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import org.joml.Matrix4f;

public class RenderUtil extends GuiComponent {
    public static void drawRect(PoseStack pose, int x, int y, int w, int h, int packedColor) {
        GuiComponent.fill(pose, x, y, x + w, y + h, packedColor);
    }

    public static void drawGuiElementDebugRect(PoseStack pose, GuiElement element, int packedColor) {
        RenderUtil.drawRect(pose, element.getGlobalX(), element.getGlobalY(), element.getWidth(), element.getHeight(), packedColor);
    }

    public static void drawTextTruncated(PoseStack pose, Font font, String text, int x, int y, int maxWidth, int packedColor) {
        int width = font.width(text);
        if (width <= maxWidth) {
            font.draw(pose, text, x, y, packedColor);
            return;
        }

        String subStr = font.plainSubstrByWidth(text, maxWidth - 9);
        font.draw(pose, subStr + "...", x, y, packedColor);
    }

    public static void putQuad(Matrix4f mat, BufferBuilder bb, int x, int y, int w, int h, int packedColor) {
        float a = (float)(packedColor >> 24 & 255) / 255.0F;
        float r = (float)(packedColor >> 16 & 255) / 255.0F;
        float g = (float)(packedColor >> 8 & 255) / 255.0F;
        float b = (float)(packedColor & 255) / 255.0F;

        bb.vertex(mat, (float)x, (float)y, 0.0F).color(r, g, b, a).endVertex();
        bb.vertex(mat, (float)x, (float)y+h, 0.0F).color(r, g, b, a).endVertex();
        bb.vertex(mat, (float)x+w, (float)y+h, 0.0F).color(r, g, b, a).endVertex();
        bb.vertex(mat, (float)x+w, (float)y, 0.0F).color(r, g, b, a).endVertex();
    }

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
