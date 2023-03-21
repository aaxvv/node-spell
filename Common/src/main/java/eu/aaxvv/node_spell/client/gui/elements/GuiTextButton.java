package eu.aaxvv.node_spell.client.gui.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class GuiTextButton extends GuiElement {
    private Component text;
    private Runnable clickCallback;
    private int backgroundColor;

    public GuiTextButton(int width, int height, Component text) {
        super(Minecraft.getInstance().font.width(text) + 4, height);
        this.text = text;
    }

    public void setText(Component text) {
        this.text = text;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setClickCallback(Runnable clickCallback) {
        this.clickCallback = clickCallback;
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        boolean hovered = this.containsPointGlobal(mouseX, mouseY);

        RenderUtil.drawRect(pose, this.getGlobalX(), this.getGlobalY(), this.getWidth(), this.getHeight(), this.backgroundColor);
        Minecraft.getInstance().font.draw(pose, text, this.getGlobalX() + 2, this.getGlobalY() + 2, 0xFF000000);

        if (hovered) {
            RenderUtil.drawRect(pose, this.getGlobalX(), this.getGlobalY(), this.getWidth(), this.getHeight(), 0x33000000);
        }

        super.render(pose, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean onMouseDown(double screenX, double screenY, int button) {
        if (this.clickCallback != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            this.clickCallback.run();
            return true;
        }

        return super.onMouseDown(screenX, screenY, button);
    }
}
