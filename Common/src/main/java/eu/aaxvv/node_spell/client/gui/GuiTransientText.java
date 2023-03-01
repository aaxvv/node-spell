package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class GuiTransientText extends GuiElement{
    private static final int FADE_OUT_TICKS = 10;
    private float ticksLeft;
    private Component currentText;
    private boolean centered;

    public GuiTransientText(int width) {
        super(width, 9);
        this.ticksLeft = 0;
        this.centered = false;
        this.currentText = null;
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
    }

    public void show(Component text, int ticks) {
        this.currentText = text;
        this.ticksLeft = ticks;
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        if (this.currentText != null && this.ticksLeft > 0) {
            int textWidth = Minecraft.getInstance().font.width(this.currentText);
            int x = this.centered ? (this.getGlobalX() + (this.getWidth() / 2)) - (textWidth / 2) : this.getGlobalX();

            byte alpha = this.ticksLeft < FADE_OUT_TICKS ? (byte)(255 * (this.ticksLeft / FADE_OUT_TICKS)) : (byte)255;
            Minecraft.getInstance().font.draw(pose, this.currentText, x, this.getGlobalY(), 0xFFFFFF | (alpha << 24));
            this.ticksLeft -= tickDelta;
        }
    }
}
