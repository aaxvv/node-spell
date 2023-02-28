package eu.aaxvv.node_spell.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.gui.GuiContext;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class BaseScreen extends Screen {
    private int x;
    private int y;

    protected final GuiContext guiContext;

    public BaseScreen(Component title) {
        super(title);
        this.guiContext = new GuiContext(this);
    }

    @Override
    protected void init() {
        this.x = (this.width / 2) - (this.getRootWidth() / 2);
        this.y = (this.height / 2) - (this.getRootHeight() / 2);

        if (this.guiContext.getRoot() != null) {
            this.guiContext.getRoot().setLocalPosition(this.x, this.y);
        }
    }

    public int getRootWidth() {
        return this.guiContext.getRoot() == null ? 0 : this.guiContext.getRoot().getWidth();
    }

    public int getRootHeight() {
        return this.guiContext.getRoot() == null ? 0 : this.guiContext.getRoot().getHeight();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        GuiElement rootElement = this.guiContext.getRoot();
        if (rootElement != null) {
            rootElement.render(pose, mouseX, mouseY, tickDelta);
        }
        this.guiContext.runRenderLast(pose, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (guiContext.onKeyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double screenX, double screenY, int buttons) {
        return guiContext.onMouseDown(screenX, screenY, buttons);
    }

    @Override
    public boolean mouseReleased(double screenX, double screenY, int buttons) {
        return guiContext.onMouseUp(screenX, screenY, buttons);
    }

    @Override
    public boolean mouseDragged(double screenX, double screenY, int buttons, double dX, double dY) {
        return super.mouseDragged(screenX, screenY, buttons, dX, dY);
    }

    @Override
    public boolean mouseScrolled(double screenX, double screenY, double amount) {
        return super.mouseScrolled(screenX, screenY, amount);
    }

    @Override
    public void mouseMoved(double dX, double dY) {
        super.mouseMoved(dX, dY);
    }

    @Override
    public boolean charTyped(char character, int modifiers) {
        return super.charTyped(character, modifiers);
    }
}
