package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import net.minecraft.network.chat.Component;

public class GuiTextureButton extends GuiElement {
    private Component tooltip;
    private Runnable clickCallback;
    private TextureRegion texture;

    public GuiTextureButton(int width, int height) {
        super(width, height);
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

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        boolean hovered = this.containsPointGlobal(mouseX, mouseY);

        if (this.texture != null) {
            this.texture.blit(pose, this.getGlobalX(), this.getGlobalY());
        }

        if (hovered) {
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
