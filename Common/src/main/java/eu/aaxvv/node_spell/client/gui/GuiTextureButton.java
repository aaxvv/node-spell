package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import net.minecraft.network.chat.Component;

public class GuiTextureButton extends GuiElement {
    private Component tooltip;
    private Runnable clickCallback;
    private TextureRegion texture;
    private int xTextureOffset;
    private int yTextureOffset;
    private boolean drawHoverOverlay;

    public GuiTextureButton(int width, int height) {
        super(width, height);
        this.xTextureOffset = 0;
        this.yTextureOffset = 0;
        this.drawHoverOverlay = true;
    }

    public void setTooltip(Component tooltip) {
        this.tooltip = tooltip;
    }

    public void setClickCallback(Runnable callback) {
        this.clickCallback = callback;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTextureOffset(int xOffset, int yOffset) {
        this.xTextureOffset = xOffset;
        this.yTextureOffset = yOffset;
    }

    public void setDrawHoverOverlay(boolean drawHoverOverlay) {
        this.drawHoverOverlay = drawHoverOverlay;
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        boolean hovered = this.containsPointGlobal(mouseX, mouseY);

        if (this.texture != null) {
            this.texture.blitOffset(pose, this.getGlobalX(), this.getGlobalY(), this.xTextureOffset, this.yTextureOffset);
        } else {
            RenderUtil.drawGuiElementDebugRect(pose, this, 0xFFFF00FF);
        }

        if (hovered && this.drawHoverOverlay) {
            RenderUtil.drawRect(pose, this.getGlobalX(), this.getGlobalY(), this.getWidth(), this.getHeight(), 0x33000000);
        }

        if (hovered && this.tooltip != null) {
            GuiContext context = this.getContext();
            context.scheduleRenderLast((_pose, mX, mY, _tickDelta) -> context.getParentScreen().renderTooltip(_pose, this.tooltip, mX, mY));
        }
    }

    @Override
    public boolean onMouseDown(double screenX, double screenY, int button) {
        if (this.clickCallback != null) {
            this.clickCallback.run();
            return true;
        }

        return super.onMouseDown(screenX, screenY, button);
    }
}
